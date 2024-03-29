package com.smartfore.admin.user;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.smartfore.common.entity.Role;
import com.smartfore.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userAmartya = new User("amartyaghosh48@gmail.com", "amartya2024", "Amartya", "Ghosh");
		userAmartya.addRole(roleAdmin);
		
		User savedUser = repo.save(userAmartya);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userAshwini = new User("ashwinidua03@gmail.com", "ashwini2024", "Ashwini", "Dua");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userAshwini.addRole(roleEditor);
		userAshwini.addRole(roleAssistant);
		
		User savedUser = repo.save(userAshwini);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
		
	}
	
	@Test
	public void testGetUserById() {
		User userAmartya  = repo.findById(1).get();
		System.out.println(userAmartya);
		assertThat(userAmartya).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {    
		User userAmartya = repo.findById(1).get();
		
		userAmartya.setEmail("gamartya02@gmail.com");
		
		repo.save(userAmartya);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userAshwini = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userAshwini.getRoles().remove(roleEditor);
		userAshwini.addRole(roleSalesperson);
		
		repo.save(userAshwini);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "amartyaghosh48@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 12;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 14;
		repo.updateEnableStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 1;
		repo.updateEnableStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "bruce";
		
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isGreaterThan(0);
	}
	
}
