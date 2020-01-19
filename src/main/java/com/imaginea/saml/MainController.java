package com.imaginea.saml;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/")
    public String landing(Authentication authentication, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            logger.info("Current authentication instance from security context is null");
        else
            logger.info("Current authentication instance from security context: " + this.getClass().getSimpleName());
        
        
        logger.info("*****");
        logger.info(auth.getCredentials().toString());
        
        SAMLCredential credential = (SAMLCredential) auth.getCredentials();
        logger.info(credential.getNameID().getValue());
        logger.info(credential.getAttributeAsString("UserID"));

        List<Attribute> attributes = credential.getAttributes();
        for (Attribute attr : attributes) {
            logger.info(attr.getName());
        }
     
        
        
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        authorities.add(authority);

        // In a real scenario, this implementation has to locate user in a arbitrary
        // dataStore based on information present in the SAMLCredential and
        // returns such a date in a form of application specific UserDetails object.
        User user = new User(credential.getNameID().getValue(), "<abc123>", true, true, true, true, authorities);

        
        
        model.addAttribute("username",  user.getUsername());
        return "index";
    }

}
