package com.guitar.db;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.guitar.db.repository.LocationJpaRepository;
import org.aspectj.lang.annotation.AfterReturning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.Location;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class LocationPersistenceTests {

	@Autowired
	private LocationJpaRepository locationJpaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void findAllAndMakeSureIsThere() {
		List<Location> locations = locationJpaRepository.findAll();
		System.out.println(locations.get(0).getState());
		assert(locations.size() > 0);
	}

	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		Location location = new Location();
		location.setCountry("Canada");
		location.setState("British Columbia");
		location = locationJpaRepository.saveAndFlush(location);
		
		// clear the persistence context so we don't return the previously cached location object
		// this is a test only thing and normally doesn't need to be done in prod code
		//entityManager.clear();

		//Location otherLocation = locationRepository.find(location.getId());
		Location otherLocation = locationJpaRepository.findOne(location.getId());
		assertEquals("Canada", otherLocation.getCountry());
		assertEquals("British Columbia", otherLocation.getState());
		
		//delete BC location now
		//locationRepository.delete(otherLocation);
		locationJpaRepository.delete(otherLocation);
	}

	@Test
	public void testFindWithLike() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateLike("New%");  //locationRepository.getLocationByStateName("New");
		for (Location loc : locs) {
			System.out.println( loc.getCountry() + loc.getState() + loc.getId().toString());
		}

		assertEquals(4, locs.size());

		locs = locationJpaRepository.findByStateStartingWithIgnoreCase("Ma");
		assertEquals(3, locs.size());
	}

	@Test
	public void testFindNotLike() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateNotLikeOrderByStateAsc("New%");  //locationRepository.getLocationByStateName("New");
		for (Location loc : locs) {
			System.out.println( loc.getCountry() + loc.getState() + loc.getId().toString());
		}

		locs.forEach((location -> {
			System.out.println(location.getState());
		}));

		assertEquals(46, locs.size());

	}

	@Test
	@Transactional  //note this is needed because we will get a lazy load exception unless we are in a tx
	public void testFindWithChildren() throws Exception {
		Location arizona = locationJpaRepository.findOne(3L);
		assertEquals("United States", arizona.getCountry());
		assertEquals("Arizona", arizona.getState());
		
		assertEquals(1, arizona.getManufacturers().size());
		
		assertEquals("Fender Musical Instruments Corporation", arizona.getManufacturers().get(0).getName());
	}

	@Test
	public void testByStateOrCountry() {
		List<Location> locations = locationJpaRepository.findByStateOrCountry("Utah", "United States");

		assertEquals(50, locations.size());

	}

	@Test
	public void testByStateAndCountry() {
		List<Location> locations = locationJpaRepository.findByStateAndCountry("Utah", "United States");

		assertEquals(1, locations.size());
		assertEquals("Utah", locations.get(0).getState());
	}

}
