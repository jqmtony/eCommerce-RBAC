package org.ecommerce.rbac.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.ecommerce.rbac.dao.SecurityObjectsDao;
import org.ecommerce.rbac.persistence.entities.SecurityObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
Copyright (C) 2001 by Radu Viorel Cosnita

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/

/**
 * SecurityObjects DAO implementation.
 * 
 * @author Radu Viorel Cosnita
 * @version 1.0
 * @since 05.10.2011
 */

@Repository("securityObjectsDaoBean")
public class SecurityObjectsDaoImpl implements SecurityObjectsDao {
	private final static Logger logger = Logger.getLogger(SecurityObjectsDaoImpl.class.getName());
	
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SecurityObject> loadAllObjects() {
		logger.info("JPA loading all security objects.");
		
		Query query = getEntityManager().createNamedQuery("SecurityObject.loadAll");
		
		return query.getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SecurityObject loadObjectById(Integer objectId) {
		logger.info(String.format("JPA loading object %s.", objectId));
		
		SecurityObject object = getEntityManager().find(SecurityObject.class, objectId);
		
		if(object == null) {
			throw new NoResultException(String.format("Object %s not found.", objectId));
		}
		
		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void createNewObject(SecurityObject object) {
		logger.info(String.format("JPA creating new object %s.", object.getName()));
		
		if(object.getId() != null) {
			throw new UnsupportedOperationException("You must not specify object id.");
		}
		
		getEntityManager().persist(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void updateObject(SecurityObject object) {
		logger.info(String.format("JPA creating new object %s.", object.getName()));
		
		if(object.getId() == null) {
			throw new UnsupportedOperationException("You must specify object id.");
		}
		
		getEntityManager().merge(object);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void removeObject(Integer objectId) {
		logger.info(String.format("JPA removing object %s.", objectId));
		
		SecurityObject object = getEntityManager().find(SecurityObject.class, objectId);
		
		if(object == null) {
			throw new NoResultException(String.format("Object %s not found.", object));
		}
		
		getEntityManager().remove(object);
	}
	
	
}