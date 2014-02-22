/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.ext.core.app.loadClient;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.ext.core.app.LoadClientService;
import com.haulmont.ext.core.entity.*;
import com.haulmont.ext.core.entity.Enum.VillaEnum;
import com.haulmont.taskman.core.enums.SexEnum;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahdi on 1/20/14.
 */
@Service(LoadClientService.NAME)
public class LoadClientServiceBean implements LoadClientService {

    @Inject
    Persistence persistance;

    @Override
    public void loadFromFile(String path) throws IOException {
        InputStreamReader inputStream = new FileReader(path);
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(inputStream);
        String line = "";
        List<String[]> lines = new ArrayList<String[]>();
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line.split(";"));
        }
        createSuperTransaction(lines);
    }

    //Разбиение коллекции на массивы-элементы
    //Разбиение каждого массива-элемента на составляющие его объекты
    //Выборка объектов для сущностей, которые к ним относятся
    private void createSuperTransaction(List<String[]> lines) {
        Republic republic = null;
        Aread aread = null;
        Villa villa = null;
        CompanyExt companyExt = null;
        for(String[] l:lines) {

            String republicS = l[0];
            if (!republicS.equals("")) republic = findRepublic(republicS);

            String areadS = l[1];
            if (!areadS.equals("")) {
                aread = findAread(areadS);
                if(aread == null) aread = createAread(areadS, republic);
            }

            String[] villaS = new String[3];
            villaS[0] = l[2];
            villaS[1] = l[3];
            villaS[2] = l[4];
            if (!villaS[0].equals("") || !villaS[2].equals("")) {
                villa = findVilla(villaS, republic);
                if (villa == null) {
                    if (!villaS[0].equals(""))
                        villa = createVilla(villaS, republic, aread);
                    if (!villaS[2].equals(""))
                        villa = createVilla(villaS, republic, null);
                }
            }

            String[] companyS = new String[6];
            try {
                companyS[0] = l[5];
                companyS[1] = l[6];
                companyS[2] = l[7];
                companyS[3] = l[8];
                companyS[4] = l[9];
                companyS[5] = l[10];
            } catch (Exception e)  {
                if (companyS[2] == null) {
                    companyS[2] = "";
                }
                //И дальше...
            }
            if (!companyS[1].equals("") && companyS[2].equals(""))
                companyExt = createCompany(companyS, villa, null);
            if (!companyS[1].equals("") && !companyS[2].equals(""))
                companyExt = createCompany(companyS, villa, findParentCompany(companyS[2]));

            String[] clientS = new String[l.length];
            for(int i = 0; i+11<l.length; i++){
                clientS[i] = l[i+11];
            }

            createClient(clientS, companyExt);
            clientS.toString();
        }
    }

    //Поиск данной Республики в бд
    private Republic findRepublic(String line) {
        Transaction tx = persistance.createTransaction();
        Republic republic = null;
        try {
            EntityManager em = persistance.getEntityManager();
            Query query = em.createQuery("select r from ext$Republic r where r.regionname = ?1");
            query.setParameter(1, line);
            republic =(Republic)query.getSingleResult();
            tx.commit();
        } finally {
            tx.end();
        }
        return republic;
    }

    //Поиск данного Района в бд
    private Aread findAread(String line) {
        Transaction tx = persistance.createTransaction();
        Aread aread = null;
        try {
            try {
                EntityManager em = persistance.getEntityManager();
                Query query = em.createQuery("select a from ext$Aread a where a.areadname =?1");
                query.setParameter(1, line);
                aread = (Aread)query.getSingleResult();
                tx.commit();
            } catch(NoResultException e) {
                tx.end();
                return null;
            }
        } finally {
            tx.end();
        }
        return aread;
    }

    //Добавление записи - Район, на основе республики, если в бд эта запись отсутствует
    private Aread createAread(String line, Republic republic) {
        Transaction tx = persistance.createTransaction();
        Aread aread = new Aread();
        try {
            EntityManager em = persistance.getEntityManager();
            aread.setAreadname(line);
            aread.setRepublic(republic);
            em.persist(aread);
            tx.commit();
        } finally {
            tx.end();
        }
        return aread;
    }

    //Поиск данного Населенного пункта в бд
    private Villa findVilla(String[] lineS, Republic republic) {
        Transaction tx = persistance.createTransaction();
        Villa villa = null;
        try {
            try {
                EntityManager em = persistance.getEntityManager();
                Query query = em.createQuery("select v from ext$Villa v where v.villaname =?1 and v.republic.id = ?2");
                if (!lineS[0].equals("")) query.setParameter(1, lineS[0]);
                else query.setParameter(1, lineS[2]);
                query.setParameter(2, republic);
                villa = (Villa) query.getSingleResult();
                tx.commit();
            } catch(NoResultException e) {
                tx.end();
                return null;
            }
        } finally {
            tx.end();
        }
        return villa;
    }

    //Добавление записи - Населенный пункт, на основе республики и района, если в бд эта запись отсутствует
    private Villa createVilla(String[] lineS, Republic republic, Aread aread) {
        Transaction tx = persistance.createTransaction();
        Villa villa = new Villa();
        try {
            EntityManager em = persistance.getEntityManager();
            if (aread != null) {
                villa.setRepublic(republic);
                villa.setAread(aread);
                villa.setTypeVilla(VillaEnum.VILLAGE);
                villa.setVillaname(lineS[0]);
                if (!lineS[1].equals("")) villa.setAreaCenter(true);
            } else {
                villa.setRepublic(republic);
                villa.setTypeVilla(VillaEnum.CITY);
                villa.setVillaname(lineS[2]);
            }
            em.persist(villa);
            tx.commit();
        } finally {
            tx.end();
        }
        return villa;
    }

    //Добавление записи - Юридическое лицо, на основании республики, района, населенного пункта
    private CompanyExt createCompany(String[] lineS, Villa villa, CompanyExt parentCompanyExt) {
        Transaction tx = persistance.createTransaction();
        CompanyExt companyExt = new CompanyExt();
        try {
            EntityManager em = persistance.getEntityManager();
            try {
                companyExt.setFullName(lineS[0]);
                companyExt.setName(lineS[1]);
                companyExt.setPostalAddress(lineS[3]);
                companyExt.setLegalAddress(lineS[4]);
                companyExt.setOkpo(lineS[5]);
            } catch (Exception e) {
                // Дальше, дальше...
            }
            companyExt.setRepublic(villa.getRepublic());
            if (villa.getAread() != null) companyExt.setAread(villa.getAread());
            if (villa != null) companyExt.setVilla(villa);
            if (parentCompanyExt != null) companyExt.setParentCompanyExt(parentCompanyExt);
            em.persist(companyExt);
            tx.commit();
        } finally {
            tx.end();
        }
        return companyExt;
    }

    //Поиск Юридического лица, как родителя, в бд
    private CompanyExt findParentCompany(String strCompanyExt) {
        Transaction tx = persistance.createTransaction();
        CompanyExt companyExt;
        try {
            EntityManager em = persistance.getEntityManager();
            Query query = em.createQuery("select c from ext$CompanyExt c where c.name = ?1");
            query.setParameter(1, strCompanyExt);
            companyExt =(CompanyExt) query.getSingleResult();
            tx.commit();
        } finally {
            tx.end();
        }
        return companyExt;
    }

    //Добавление записи - Клиент, на основании юридического лица
    private void createClient(String[] lineS, CompanyExt companyExt) {
        Transaction tx = persistance.createTransaction();
        ExtClient extClient = new ExtClient();
        try {
            EntityManager em = persistance.getEntityManager();
            try {
                extClient.setExtCompany(companyExt);
                extClient.setSurnameC(lineS[0]);
                extClient.setNameC(lineS[1]);
                extClient.setPatronymicC(lineS[2]);
                if (!lineS[1].equals("") && !lineS[2].equals(""))
                    extClient.setName(lineS[0] + " " + lineS[1].substring(0,1) + ". " + lineS[2].substring(0,1) + ".");
                if (!lineS[1].equals("") && lineS[2].equals(""))
                    extClient.setName(lineS[0] + " " + lineS[1]);
                if (lineS[1].equals("") && !lineS[2].equals(""))
                    extClient.setName(lineS[0] + " " + lineS[2]);
                extClient.setName(lineS[0]);
                extClient.setPost(lineS[3]);
                extClient.setNumberTel(lineS[4]);
                extClient.setNumberTw(lineS[5]);
                extClient.setPlaceBirth(lineS[6]);
                extClient.setCountChs(lineS[7]);
                if (lineS[8] == "жен") extClient.setSex(SexEnum.FEMALE);
                else extClient.setSex(SexEnum.MALE);
                extClient.setSurnameR(lineS[9]);
                extClient.setNameR(lineS[10]);
                extClient.setPatronymicR(lineS[11]);
                extClient.setBank(lineS[12]);
                extClient.setInn(lineS[13]);
                extClient.setKpp(lineS[14]);
                extClient.setRs(lineS[15]);
                extClient.setKs(lineS[16]);
                extClient.setBik(lineS[17]);
                extClient.setLs(lineS[18]);
            }  catch (Exception e) {
                //Не все так плохо, двигайся дальше...
            }
            em.persist(extClient);
            tx.commit();
        } finally {
            tx.end();
        }
    }
}