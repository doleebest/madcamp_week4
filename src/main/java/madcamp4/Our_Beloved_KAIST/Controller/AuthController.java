package madcamp4.Our_Beloved_KAIST.Controller;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @GetMapping("/public/test")
    public String publicEndpoint() {
        return "This is public endpoint";
    }

    @GetMapping("/private/user")
    public String privateEndpoint(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        return "Hello, User " + uid;
    }
}