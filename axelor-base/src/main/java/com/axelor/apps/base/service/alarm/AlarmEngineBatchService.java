/**
 * Axelor Business Solutions
 *
 * Copyright (C) 2015 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.base.service.alarm;

import java.lang.reflect.Field;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.apps.base.db.Alarm;
import com.axelor.apps.base.db.AlarmEngine;
import com.axelor.apps.base.db.repo.AlarmRepository;
import com.axelor.apps.base.exceptions.IExceptionMessage;
import com.axelor.apps.base.service.administration.AbstractBatch;
import com.axelor.db.JPA;
import com.axelor.db.Model;
import com.axelor.exception.service.TraceBackService;
import com.axelor.i18n.I18n;
import com.google.inject.persist.Transactional;

public class AlarmEngineBatchService extends AbstractBatch {

	static final Logger LOG = LoggerFactory
			.getLogger(AlarmEngineBatchService.class);
	
	protected AlarmEngineService<Model> alarmEngineService;
	
	@Inject
	private AlarmRepository alarmRepo;
	
	@Inject
	public AlarmEngineBatchService (AlarmEngineService<Model> alarmEngineService) {
		
		this.alarmEngineService = alarmEngineService; 
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process() {
				
		for (AlarmEngine alarmEngine : batch.getAlarmEngineBatch().getAlarmEngineSet()) {
			
			try {
				
				persistAlarm (
					alarmEngineService.get( 
						alarmEngine, (Class<Model>)Class.forName( alarmEngine.getMetaModel().getFullName() ) 
					)
				);
				

			} catch (Exception e) {

				TraceBackService.trace(new Exception(String.format(I18n.get(IExceptionMessage.ALARM_ENGINE_BATCH_1), alarmEngine.getCode()), e), "", batch.getId());
				incrementAnomaly();

			} finally {
				
				JPA.clear();
				
			}

		}		
		
	}

	@Override
	protected void stop() {

		String comment = I18n.get(IExceptionMessage.ALARM_ENGINE_BATCH_2);
		comment += String.format(I18n.get(IExceptionMessage.ALARM_ENGINE_BATCH_3), batch.getDone() );
		comment += String.format(I18n.get(IExceptionMessage.ALARM_ENGINE_BATCH_4), batch.getAnomaly() );
		
		super.stop();
		addComment(comment);
		
		
	}
	
	@Transactional
	protected <T extends Model> void persistAlarm(Map<T, Alarm> alarms) throws IllegalArgumentException, IllegalAccessException{
		
		Alarm alarm = null;
		for (T t : alarms.keySet()) {
			
			alarm = alarms.get(t); associateAlarm(alarm, t);
			alarmRepo.save(alarm); incrementDone();
		}
		
	}
	
	private <T extends Model> void associateAlarm(Alarm alarm, T t) throws IllegalArgumentException, IllegalAccessException{
		
		LOG.debug("ASSOCIATE alarm:{} TO model:{}", new Object[] { batch, model });
		
		for (Field field : alarm.getClass().getDeclaredFields()){
		
			LOG.debug("TRY TO ASSOCIATE field:{} TO model:{}", new Object[] { field.getType().getName(), t.getClass().getName() });
			if ( isAssociable(field, t) ){
				
				LOG.debug("FIELD ASSOCIATE TO MODEL");
				field.setAccessible(true);
				field.set(alarm, t);
				field.setAccessible(false);
				
				break;
				
			}
			
		}
		
	}
	
	private <T extends Model> boolean isAssociable(Field field, T t){
		
		return field.getType().equals( persistentClass(t) );
		
	}
	
	private <T extends Model> Class<?> persistentClass(T t){
		
		if (t instanceof HibernateProxy) {
		      return ((HibernateProxy) t).getHibernateLazyInitializer().getPersistentClass();
		}
		else { return t.getClass(); }
		
	}

}
