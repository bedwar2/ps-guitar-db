package com.guitar.db;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.guitar.db.repository.ModelJpaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.Model;
import com.guitar.db.repository.ModelRepository;

import static org.junit.Assert.*;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ModelPersistenceTests {
	@Autowired
	private ModelRepository modelRepository;

	@Autowired
	private ModelJpaRepository jpaRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		Model m = new Model();
		m.setFrets(10);
		m.setName("Test Model");
		m.setPrice(BigDecimal.valueOf(55L));
		m.setWoodType("Maple");
		m.setYearFirstMade(new Date());
		m = modelRepository.create(m);
		
		// clear the persistence context so we don't return the previously cached location object
		// this is a test only thing and normally doesn't need to be done in prod code
		//entityManager.clear();

		Model otherModel = modelRepository.find(m.getId());
		assertEquals("Test Model", otherModel.getName());
		assertEquals(10, otherModel.getFrets());
		
		//delete BC location now
		modelRepository.delete(otherModel);

		jpaRepo.aCustomMethod();
	}

	@Test
	public void testGetModelsInPriceRange() throws Exception {
		List<Model> mods = modelRepository.getModelsInPriceRange(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2000L));
		assertEquals(4, mods.size());
	}

	@Test
	public void testGetModelsByPriceRangeAndWoodType() throws Exception {
		Page<Model> mods = modelRepository.getModelsByPriceRangeAndWoodType(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2000L), "Maple");
		mods.forEach((model -> {
			System.out.println(model.getName());
		}));
		assertEquals(2, mods.getSize());

		System.out.println("Size = " + mods.getSize());
	}

	@Test
	public void testGetModelsByType() throws Exception {
		List<Model> mods = modelRepository.getModelsByType("Electric");
		assertEquals(4, mods.size());
	}

	@Test
	public void findAllAcoustic() {
		List<String> modelTypes = new ArrayList<>();
		modelTypes.add("Dreadnought Acoustic");
		modelTypes.add("Nylon String Acoustic");
		modelTypes.add("Acoustic Electric");
		List<Model> mods = jpaRepo.findByModelTypeNameIn(modelTypes);
		System.out.println("Total Recs: " + mods.size());
		assertNotNull(mods);

		mods.forEach((model) -> {
			System.out.println(model.getModelType().getName());
			assertTrue(model.getModelType().getName().equals("Dreadnought Acoustic")
				|| model.getModelType().getName().equals("Nylon String Acoustic")
				|| model.getModelType().getName().equals("Acoustic Electric")
			);
		});
	}

	@Test
	public void TestModelCount() {
		Long count = modelRepository.getModelCount();
		assertTrue(count > 0);
		System.out.println("Total Count: " + count);
	}
}
