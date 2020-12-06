package ru.guteam.customer_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.guteam.customer_service.entities.User;
import ru.guteam.customer_service.entities.Role;
import ru.guteam.customer_service.entities.utils.RestaurantInfo;
import ru.guteam.customer_service.entities.utils.SystemUser;
import ru.guteam.customer_service.repositories.UsersRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService implements UserDetailsService {
    private UsersRepository usersRepository;
    private RolesService rolesService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setRolesService(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
                true, true, user.isEnable(), mapRolesToAuthorities(Arrays.asList(user.getRole())));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findOneByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Невозможно найти пользователя по логину = " + username));
    }

    public Optional<User> findOptionalById(Long id) {
        return usersRepository.findOneById(id);
    }

    public User createBySystemCustomer(SystemUser systemUser) {
        User user = new User();
        user.setUsername(systemUser.getUsername());
        user.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        user.setRole(rolesService.findByName("CUSTOMER"));
        user.setEnable(true);
        return user;
    }

    public boolean existsByUsername(String username) {
        return usersRepository.existsByUsername(username);
    }

    @Transactional
    public void updateRestaurantId(RestaurantInfo info) {
        final User user = findOptionalById(info.getUserId()).get();
        user.setRestaurantId(info.getRestaurantId());
        user.setRole(rolesService.findByName(info.getRoleName()));
        usersRepository.save(user);
    }
}