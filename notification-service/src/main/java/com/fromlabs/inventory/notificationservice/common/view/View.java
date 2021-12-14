/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.view;

/**
 * View distillation for data response
 */
public interface View {
    public static interface Web {
        public static class Public {}
        public static class Internal extends Public {}
        public static class Admin extends Internal {}
    }

    public static interface Mobile {
        public static class Public {}
        public static class Internal extends Public {}
        public static class Admin extends Internal {}
    }
}
