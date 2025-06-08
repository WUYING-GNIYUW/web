//package authorizationServer.util;
//
//import authorizationServer.security.DBUserDetailsManager;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//
//public class JwtUtil {
//    private static final JwtConstant jwtConstant;
//    private static final DBUserDetailsManager dbuserDetailsManager;
//    private JwtUtil(){}
//    static {
//        jwtConstant = (JwtConstant) SpringContextUtil.getBean(JwtConstant.class);
//        dbuserDetailsManager = (DBUserDetailsManager) SpringContextUtil.getBean(DBUserDetailsManager.class);
//    }
//    public static String createJwtToken(){
////        User user = dbuserDetailsManager.loadUserByUserid(UseridThread.get());
////        Map<String,Object> claims = new HashMap<>();
//////        claims.put();
//        return Jwts.builder()
////                .setSubject(user.getUserid())
////                .signWith(SignatureAlgorithm.HS256, jwtConstant.getSigning_key())
////                .setClaims(claims)
////                .setIssuedAt(new Date(System.currentTimeMillis()))
////                .setExpiration(new Date(System.currentTimeMillis() + 3*3600*1000))
//                .compact();
//    }
//    public static Claims getClamis(String token) {
//        return Jwts.parser()
//                .setSigningKey(jwtConstant.getSigning_key())
//                .parseClaimsJws(token)
//                .getBody();
//    }
//    public static String getUserid(String token) {
//        return getClamis(token).getSubject();
//    }
//}
