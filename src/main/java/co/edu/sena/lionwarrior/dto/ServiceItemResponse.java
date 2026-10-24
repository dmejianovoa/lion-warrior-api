package co.edu.sena.lionwarrior.dto;

import co.edu.sena.lionwarrior.model.ServiceItem;
import java.math.BigDecimal;

/** Servicio tal como lo ve el cliente. */
public record ServiceItemResponse(

        Integer idService,
        String serviceName,
        String serviceDescription,
        BigDecimal servicePrice,
        Integer durationMin
) {
    public static ServiceItemResponse from(ServiceItem item) {
        return new ServiceItemResponse(item.getIdService(), item.getServiceName(), item.getServiceDescription(),
                                      item.getServicePrice(), item.getDurationMin());
    }
}
