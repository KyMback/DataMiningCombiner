package evm.dmc.business.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ActiveProfiles("test")
public class AccountRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;

	@MockBean
	private AccountService mockAccService;
	
	@Autowired
    private AccountRepository repository;

	@Before
	public void init() {
		this.entityManager.persist(new AccountExt("id42cat", "password", "id42cat@tut.by", "Alex", "Demidchuk", "ROLE_ADMIN").getAccount());
		this.entityManager.persist(new AccountExt("admin3", "password", "admin@mail.org", "Admin", "AD_min", "ROLE_ADMIN").getAccount());
	}
	
	@Test
	public final void testFindByEmail() throws Exception {
		Account acc = this.repository.findByEmail("admin@mail.org").orElseThrow(()->new Exception(""));
		assertThat(acc.getFirstName()).isEqualTo("Admin");
	}
	
	@Test
	public final void testFindByUserName() throws Exception {
		Account acc = this.repository.findByUserName("id42cat").orElseThrow(()->new Exception(""));
		assertThat(acc.getFirstName()).isEqualTo("Alex");
	}
	
	@Test(expected = Exception.class)
	public final void testFindByNotExistingUserName() throws Exception {
		Account acc = this.repository.findByUserName("_").orElseThrow(()->new Exception(""));
		assertThat(acc.getFirstName()).isEqualTo("Alexx");
	}

	@Test
	public final void testFindByRoleString() {
		List<Account> accCollection = this.repository.findByRole("ROLE_ADMIN");
		assertThat(accCollection).isNotEmpty();
		assertThat(accCollection).hasSize(2);
	}

	@Test
	public final void testFindByRoleStringPageable() {
		List<Account> accCollection = this.repository.findByRole("ROLE_ADMIN", new PageRequest(0,1));
		assertThat(accCollection).hasSize(1);
		assertThat(accCollection.get(0).getUserName()).isEqualTo("id42cat");
		
		accCollection = this.repository.findByRole("ROLE_ADMIN", new PageRequest(1,1));
		assertThat(accCollection).hasSize(1);
		assertThat(accCollection.get(0).getUserName()).isEqualTo("admin3");
	}

	@Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
	public final void testAddExistingAccount() {
		repository.save(new AccountExt("id42cat", "password", "id42cat@tut.by", "Alex", "Demidchuk", "ROLE_ADMIN"));
	}
}