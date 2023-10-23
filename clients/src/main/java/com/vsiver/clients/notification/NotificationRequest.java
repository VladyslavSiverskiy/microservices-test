package com.vsiver.clients.notification;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NotificationRequest{
        private Integer toCustomerId;
        private String toCustomerName;
        private String message;

        public Integer getToCustomerId() {
                return toCustomerId;
        }

        public String getToCustomerName() {
                return toCustomerName;
        }

        public String getMessage() {
                return message;
        }
}