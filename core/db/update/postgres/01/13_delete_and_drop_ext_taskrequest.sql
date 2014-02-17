DELETE FROM ext_taskrequest^
DELETE FROM tm_task WHERE tm_task.card_id in (select wf_card.id from wf_card where wf_card.created_by = 'oktell')^
DELETE FROM wf_assignment WHERE wf_assignment.card_id in (select wf_card.id from wf_card where wf_card.created_by = 'oktell')^
DELETE FROM wf_card_info WHERE wf_card_info.card_id in (select wf_card.id from wf_card where wf_card.created_by = 'oktell')^
DELETE FROM wf_card_role WHERE wf_card_role.card_id in (select wf_card.id from wf_card where wf_card.created_by = 'oktell')^
DELETE FROM wf_card where wf_card.created_by = 'oktell'^

ALTER TABLE EXT_TASKREQUEST DROP CONSTRAINT FK_EXT_TASKREQUEST_CLIENT^
ALTER TABLE EXT_TASKREQUEST DROP CONSTRAINT FK_EXT_TASKREQUEST_TASK^
