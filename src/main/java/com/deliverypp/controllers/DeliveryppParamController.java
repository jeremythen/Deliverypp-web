package com.deliverypp.controllers;

import com.deliverypp.models.DeliveryppParam;
import com.deliverypp.services.params.DeliveryppParamService;
import com.deliverypp.util.DeliveryppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/params")
public class DeliveryppParamController {

    private DeliveryppParamService deliveryppParamService;

    @Autowired
    public DeliveryppParamController(DeliveryppParamService deliveryppParamService) {
        this.deliveryppParamService = deliveryppParamService;
    }

    @PostMapping
    public ResponseEntity<?> addParam(@Valid @RequestBody DeliveryppParam deliveryppParam) {

        DeliveryppResponse<?> response = deliveryppParamService.addParam(deliveryppParam);

        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateParam(@PathVariable int id, @Valid @RequestBody DeliveryppParam deliveryppParam) {

        deliveryppParam.setId(id);

        DeliveryppResponse<?> response = deliveryppParamService.updateParam(deliveryppParam);

        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<?> getParams() {
        DeliveryppResponse<?> response = deliveryppParamService.getParams();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getParamById(@PathVariable int id) {

        DeliveryppResponse<?> response = deliveryppParamService.getParamsById(id);

        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParamById(@PathVariable int id) {

        DeliveryppResponse<?> response = deliveryppParamService.deleteParamById(id);

        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }

    }

}
