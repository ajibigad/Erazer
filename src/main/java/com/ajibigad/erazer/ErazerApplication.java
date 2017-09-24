package com.ajibigad.erazer;

import com.ajibigad.erazer.model.expense.Expense;
import com.ajibigad.erazer.model.expense.PROOF_TYPE;
import com.ajibigad.erazer.model.expense.STATE;
import com.ajibigad.erazer.repository.expense.ExpenseRepository;
import com.ajibigad.erazer.security.model.Role;
import com.ajibigad.erazer.security.model.User;
import com.ajibigad.erazer.security.repository.RoleRepository;
import com.ajibigad.erazer.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class ErazerApplication {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ExpenseRepository expenseRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	private Logger log = LoggerFactory.getLogger(ErazerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ErazerApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			String placholderImage = "http://via.placeholder.com/150x150";

			createRoles("ROLE_ADMIN", "ROLE_USER");
			Role adminRole = roleRepository.findByName("ROLE_ADMIN");
			Role userRole = roleRepository.findByName("ROLE_USER");

			User ajibigad = new User();
			ajibigad.setUsername("ajibigad");
			ajibigad.setEmail("ajibigad@gmail.com");
			ajibigad.setPassword(passwordEncoder.encode("ajibigad"));
			ajibigad.setFirstname("Damilola");
			ajibigad.setLastname("Ajiboye");
			ajibigad.setFcmNotificationKey("APA91bHL4GrgW3eqsnuvY0DMOyTFjAF450NwO6zcBCrCaryGos2bSKJUbskksex4kPrTnMTw0APPY16TVEKkNgx_1qxbjkNjjWDjKw14X08iUpxOqhRpVms");
			ajibigad.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
			userRepository.save(ajibigad);

			User admin = new User();
			admin.setUsername("admin");
			admin.setEmail("admin@gmail.com");
			admin.setPassword(passwordEncoder.encode("adminpass"));
			admin.setFirstname("Superuser");
			admin.setLastname("Admin");
			admin.setFcmNotificationKey("APA91bEHhNHOM-E8mycGyfDEWHzl4gZ1qC6m7g_jp3OPeZs_rPr8juOK6Xo-O2IF840TwneHzFPkqDNKR-sMYv7xOL8plOvr4eLMMsQzNzhD0_pLSe31hR0");
			admin.setRoles(new HashSet<Role>(Arrays.asList(adminRole)));
			userRepository.save(admin);

			log.info(userRepository.findOne(admin.getUsername()).getFcmNotificationKey());

			Expense expense1 = new Expense();
			Expense expense2 = new Expense();
			Expense expense3 = new Expense();
			Expense expense4 = new Expense();
			Expense expense5 = new Expense();
			Expense expense6 = new Expense();
			Expense expense7 = new Expense();
			Expense expense8 = new Expense();
			Expense expense9 = new Expense();
			Expense expense10 = new Expense();

			expense1.setTitle("taxify trip");
			expense1.setCost(2345);
			expense1.setDescription("Went to UPSL for nonyabusines");
			expense1.setUser(ajibigad);
			expense1.setProofType(PROOF_TYPE.EMAIL);
			expense1.setState(STATE.PENDING);
			expenseRepository.save(expense1);

			expense2.setTitle("new laptop charger");
			expense2.setCost(135);
			expense2.setDescription("Bought a new laptop charger");
			expense2.setUser(admin);
			expense2.setProofType(PROOF_TYPE.IMAGE);
			expense2.setProof(placholderImage);
			expense2.setState(STATE.APPROVED);
			expenseRepository.save(expense2);

			expense3.setTitle("new laptop charger");
			expense3.setCost(135);
			expense3.setDescription("Bought a new laptop charger");
			expense3.setUser(admin);
			expense3.setProofType(PROOF_TYPE.TEXT);
			expense3.setProof("ask tosin");
			expense3.setState(STATE.PENDING);
			expenseRepository.save(expense3);

			expense4.setTitle("latest hp laptop");
			expense4.setCost(200035);
			expense4.setDescription("Bought a new laptop charger");
			expense4.setUser(ajibigad);
			expense4.setProofType(PROOF_TYPE.IMAGE);
			expense4.setProof(placholderImage);
			expense4.setState(STATE.DECLINED);
			expenseRepository.save(expense4);

			expense5.setTitle("new laptop charger");
			expense5.setCost(135);
			expense5.setDescription("Bought a new laptop charger");
			expense5.setUser(admin);
			expense5.setProofType(PROOF_TYPE.IMAGE);
			expense5.setProof("nonvoern");
			expense5.setState(STATE.SETTLED);
			expenseRepository.save(expense5);

			expense6.setTitle("new LAPTOP");
			expense6.setCost(135);
			expense6.setDescription("Bought a new laptop macbook");
			expense6.setUser(admin);
			expense6.setProofType(PROOF_TYPE.EMAIL);
			expense6.setProof("nonvoern");
			expense6.setState(STATE.PENDING);
			expenseRepository.save(expense6);

			expense7.setTitle("new laptop bag");
			expense7.setCost(135);
			expense7.setDescription("Bought a new laptop bag");
			expense7.setUser(admin);
			expense7.setProofType(PROOF_TYPE.IMAGE);
			expense7.setProof(placholderImage);
			expense7.setState(STATE.PENDING);
			expenseRepository.save(expense7);

			expense8.setTitle("new laptop charger");
			expense8.setCost(135);
			expense8.setDescription("Bought a new laptop charger");
			expense8.setUser(admin);
			expense8.setProofType(PROOF_TYPE.IMAGE);
			expense8.setProof(placholderImage);
			expense8.setState(STATE.APPROVED);
			expenseRepository.save(expense8);

			expense9.setTitle("new suit");
			expense9.setCost(135);
			expense9.setDescription("Bought a new suit");
			expense9.setUser(ajibigad);
			expense9.setProofType(PROOF_TYPE.IMAGE);
			expense9.setProof(placholderImage);
			expense9.setState(STATE.DECLINED);
			expenseRepository.save(expense9);

			expense10.setTitle("konga books");
			expense10.setCost(12335);
			expense10.setDescription("Purchased konga books");
			expense10.setUser(ajibigad);
			expense10.setProofType(PROOF_TYPE.EMAIL);
			expense10.setProof("nonvoern");
			expense10.setState(STATE.PENDING);
			expenseRepository.save(expense10);
		};
	}

	@Transactional
	private Role createRoleIfNotFound(String name) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			return roleRepository.save(role);
		}
		return role;
	}

	private void createRoles(String ... names){
		for (String name : names){
			createRoleIfNotFound(name);
		}
	}

}

