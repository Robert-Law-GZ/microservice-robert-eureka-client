package org.robert.robert.eureka.client.controller;

import org.robert.robert.eureka.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EurekaClientController {
    private static String ACCOUNT_SERVICE_NAME = "robert-microservice-account";
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user/{id}")
    public User findById(@PathVariable Long id) {
        List<ServiceInstance> list = discoveryClient.getInstances(ACCOUNT_SERVICE_NAME);
        User user = null;

        if (list != null && list.size() > 0) {
            ServiceInstance instance = list.get(0);
            user = restTemplate.getForObject(instance.getUri().toString() + "/user/profile/" + id, User.class);
        }

        return user;
    }

    @GetMapping("/user/list")
    public List<User> userList() {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(ACCOUNT_SERVICE_NAME);
        List<User> list = new ArrayList();

        if (serviceInstances != null && serviceInstances.size() > 0) {
            ServiceInstance instance = serviceInstances.get(0);
            list = restTemplate.getForObject(instance.getUri().toString() + "/user/list", List.class);
        }

        return list;
    }

}
