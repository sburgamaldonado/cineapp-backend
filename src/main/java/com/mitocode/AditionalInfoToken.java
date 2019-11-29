package com.mitocode;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.mitocode.model.Usuario;
import com.mitocode.service.IUsuarioService;

@Component
public class AditionalInfoToken implements TokenEnhancer{

	@Autowired
	private IUsuarioService usuarioService;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Usuario usuario = usuarioService.buscarPorUsuario(authentication.getName());
		Map<String, Object> info = new HashMap<>();
		
		info.put("Info_adicional", "Hola que tal! ".concat(authentication.getName()));
		info.put("id_usuario", usuario.getIdUsuario());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		
		return accessToken;
	}
	
	
}