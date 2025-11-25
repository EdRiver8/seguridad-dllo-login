package com.example.proyectoseguridaddllologin.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para exponer el token CSRF.
 * Los clientes frontend necesitan obtener este token antes de hacer peticiones
 * mutantes (POST, PUT, DELETE, PATCH).
 */
@RestController
@RequestMapping("/api")
public class CsrfController {

  /**
   * Endpoint público que devuelve el token CSRF.
   * El cliente debe incluir este token en el header "X-XSRF-TOKEN" para todas las
   * peticiones mutantes.
   * 
   * @param token Token CSRF inyectado automáticamente por Spring Security
   * @return El token CSRF en formato JSON
   */
  @GetMapping("/csrf-token")
  public CsrfToken getCsrfToken(CsrfToken token) {
    return token;
  }
}
