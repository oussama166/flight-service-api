package org.jetblue.jetblue.Models.DAO;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {
  private User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(user.getRole().toString()));
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  public String getEmail() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return user.isEnabled();
  }

  @Override
  public boolean isAccountNonLocked() {
    return user.isVerified();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return user.isVerified();
  }

  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }
}
