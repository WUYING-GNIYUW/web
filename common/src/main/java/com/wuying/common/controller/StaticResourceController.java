package com.wuying.common.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import com.wuying.common.util.Util;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaticResourceController {
//    @GetMapping("/pblc/sttc/{format}/{resourceName}")
//    public ResponseEntity<Void> getPublicStaticResource(@PathVariable("format") String format, @PathVariable("resourceName") String resourceName) {
//        String baseInternalUri = "/internal_protected/public/static";
////        String internalUri = baseInternalUri + Util.encodePath(format, resourceName);
//        String internalUri = Util.encodePath(format, resourceName);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("X-Accel-Redirect", internalUri);
//
//        return ResponseEntity.ok().headers(headers).build();
//    }
//
//    @GetMapping("/pivt/sttc/{format}/{resourceName}")
//    public ResponseEntity<Void> getPrivateStaticResource(@PathVariable("format") String format, @PathVariable("resourceName") String resourceName) {
//        String baseInternalUri = "/internal_protected/private/static";
////        String internalUri = baseInternalUri + Util.encodePath(format, resourceName);
//        String internalUri = Util.encodePath(format, resourceName);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("X-Accel-Redirect", internalUri);
//
//        return ResponseEntity.ok().headers(headers).build();
//    }
    @GetMapping("/getResource")
    public ResponseEntity<Void> getResource(@RequestParam("protected_level") String protectedLevel,@RequestParam("type") String type, @RequestParam("format") String format, @RequestParam("resource_name") String resourceName,@RequestParam("file_extension") String fileExtension) {
        String baseInternalUri = "/internal_protected";
        String internalUri = baseInternalUri + Util.encodePath(fileExtension, protectedLevel, type, format, resourceName);
//        String internalUri = Util.encodePath(protectedLevel, type, format, resourceName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Accel-Redirect", internalUri);
        System.out.println(internalUri);
        return ResponseEntity.ok().headers(headers).build();
    }
}
