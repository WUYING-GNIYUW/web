//package gatewayServer.test;
//
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OAuth2ClientInfo {
//
//    private final ClientRegistrationRepository clientRegistrationRepository;
//
//    public OAuth2ClientInfo(ClientRegistrationRepository clientRegistrationRepository) {
//        this.clientRegistrationRepository = clientRegistrationRepository;
//    }
//
//    public void printAllRegistrationIds() {
//        Iterable<ClientRegistration> registrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
//
//        // 遍历方式1：增强 for 循环
//        for (ClientRegistration registration : registrations) {
//            System.out.println(registration.getRegistrationId());
//        }
//    }
//}