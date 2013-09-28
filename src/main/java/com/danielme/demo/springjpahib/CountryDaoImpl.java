package com.danielme.demo.springjpahib;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository(value="countryDao")
public class CountryDaoImpl implements ICountryDao
{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public void addAll(List<Country> list)
	{
		for(Country country : list)
		{
			entityManager.persist(country);
		}		
	}

	@Override
	@Transactional
	public Country getCountryByName(String name)
	{
		//JPA Criertia
	  CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	  CriteriaQuery<Country> query = criteriaBuilder.createQuery(Country.class);

	  Root<Country> country = query.from(Country.class);
	  query.where(criteriaBuilder.equal(country.<String>get("name"),criteriaBuilder.parameter(String.class, "param")));

	  TypedQuery<Country> typedQuery = entityManager.createQuery(query);
	  typedQuery.setParameter("param", name);
	  
	  return typedQuery.getResultList().get(0);
		
		
//		//Hibernate Criteria
//	  Session session = entityManager.unwrap(Session.class);
//	  Criteria criteria = session.createCriteria(Country.class);
//	  criteria.add(Restrictions.eq("name", name));
//	  return (Country) criteria.uniqueResult();  

	}

	@Override
	@Transactional
	public void deleteAll()
	{
	     entityManager.createQuery("DELETE FROM countries").executeUpdate();		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Country> getAll()
	{
		Query query = entityManager.createQuery("from " + Country.class.getName());
		query.setHint("org.hibernate.cacheable", true);
		return query.getResultList();
	}

	@Override
	public Country getById(Long id)
	{
		return entityManager.find(Country.class, id);
	}	

}