package com.deliverypp.util;

import java.lang.annotation.*;

@Target( { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeliveryppLoggin {
}
