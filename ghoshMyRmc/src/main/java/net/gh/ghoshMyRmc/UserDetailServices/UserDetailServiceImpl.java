package net.gh.ghoshMyRmc.UserDetailServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import net.gh.ghoshMyRmcBackend.Util;
import net.gh.ghoshMyRmcBackend.dao.UserDao;
import net.gh.ghoshMyRmcBackend.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		User user = userDao.getUserByEmailId(userName);

		List<User> users = userDao.userLists();
		System.out.println("total users are [" + users.size() + "]");

		if (user != null) {
			String password = user.getPassword();
			boolean enabled = true;

			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(user.getRole()));

			org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(
					userName, password, enabled, enabled, enabled, enabled,
					authorities);
			return securityUser;
		} else {
			if (users.size() == 0) {
				System.out.println("hello there !! I am here");
				User newUser = new User();
				newUser.setEmail("admin@myrmc.com");
				newUser.setName("Admin");
				newUser.setEnabled(true);
				newUser.setPassword(passwordEncoder.encode("Admin@123"));
				newUser.setRole(Util.SUPERADMIN);
				newUser.setState(Util.ACTIVE_STATE);
				userDao.addUser(newUser);
				throw new UsernameNotFoundException("hahahahahahahahaha");
			} else {
				throw new UsernameNotFoundException("dsfgsdfgdfgdsfgsdfg");
			}
		}

	}

}
