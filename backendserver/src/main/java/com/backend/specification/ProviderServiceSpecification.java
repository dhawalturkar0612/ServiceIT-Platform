package com.backend.specification;

import org.springframework.data.jpa.domain.Specification;

import com.backend.entities.ProviderService;

import jakarta.persistence.criteria.Predicate;

/**
 * Specification class used to build dynamic search queries
 * for ProviderService.
 */
public class ProviderServiceSpecification {

    /**
     * Builds a dynamic Specification based on optional
     * service name and city.
     *
     * @param serviceName Name of the service (e.g., Plumber)
     * @param city Name of the city (e.g., Pune)
     * @return Specification<ProviderService>
     */
    public static Specification<ProviderService> searchProviderServices(
            String serviceName,
            String city) {

        return (root, query, criteriaBuilder) -> {

            Predicate predicate = criteriaBuilder.conjunction();

            // Search by Service Name
            if (serviceName != null && !serviceName.trim().isEmpty()) {

                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("service")
                                                .get("serviceName")),
                                "%" + serviceName.toLowerCase() + "%"));
            }

            // Search by Provider City
            if (city != null && !city.trim().isEmpty()) {

            	predicate = criteriaBuilder.and(
            	        predicate,
            	        criteriaBuilder.like(
            	                criteriaBuilder.lower(
            	                        root.get("provider")
            	                                .get("city")),
            	                "%" + city.toLowerCase() + "%"));
            }

            // Only Available Services
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.isTrue(root.get("available")));

            return predicate;
        };
    }
}