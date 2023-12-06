package com.registro.login.Service;

import com.registro.login.Controller.Response.UserResponse;
import com.registro.login.Entity.Enum.Role;
import com.registro.login.Entity.Establecimiento;
import com.registro.login.Entity.Enum.Role;
import com.registro.login.Entity.Request.RegisterEstRequest;
import com.registro.login.Entity.Request.RegisterRequest;
import com.registro.login.Entity.User;
import com.registro.login.Jwt.JwtService;

import com.registro.login.Repository.EstablecimientoRepository;
import com.registro.login.Repository.UserRepository;
import com.registro.login.Entity.Request.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final EstablecimientoRepository establecimientoRepository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	public UserResponse login(LoginRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
		String token = jwtService.getToken(user);
		return UserResponse.builder()
				.token(token)
				.build();
	}

	public UserResponse loginE(LoginRequest request) {
		UserDetails user = establecimientoRepository.findByUsername(request.getUsername()).orElseThrow();
		String token = jwtService.getToken(user);
		return UserResponse.builder()
				.token(token)
				.build();
	}

	public String registerUsuario(RegisterRequest request) {
			User user = User.builder()
					.username(request.getUsername())
					.password(passwordEncoder.encode(request.getPassword()))
					.correo(request.getCorreo())
					.role(Role.USER)
					.build();
			userRepository.save(user);
			return "Usuario Registrado con EXITO";
	}

	public String registerEstablecimiento(RegisterEstRequest requestEst){
		Establecimiento establecimiento = Establecimiento.builder()
				.username(requestEst.getUsername())
				.password(passwordEncoder.encode(requestEst.getPassword()))
				.correo(requestEst.getCorreo())
				.role(Role.ESTABLECIMIENTO)
				.direccion(requestEst.getDireccion())
				.numeroTelefono(requestEst.getNumeroTelefono())
				.ubicacion(requestEst.getUbicacion())
				.categoria(requestEst.getCategoria())
				.horaApertura(requestEst.getHoraApertura())
				.horaCierre(requestEst.getHoraCierre())
				.build();
		establecimientoRepository.save(establecimiento);
		return "Establecimiento Registrado con EXITO";
	}
}



