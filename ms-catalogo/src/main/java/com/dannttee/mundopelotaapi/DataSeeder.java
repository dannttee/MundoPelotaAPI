package com.dannttee.mundopelotaapi;

import com.dannttee.mundopelotaapi.model.Pelota;
import com.dannttee.mundopelotaapi.repository.PelotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private PelotaRepository pelotaRepository;

    @Override
    public void run(String... args) throws Exception {

        if (pelotaRepository.count() == 0) {
            
            // --- PELOTA 1 ---
            Pelota p1 = new Pelota();
            p1.setNombre("Al Rihla (Mundial 2022)");
            p1.setMarca("Adidas");
            p1.setPrecio(new BigDecimal("45000.00")); 
            p1.setDeporte("Futbol");
            p1.setStock(10);
            p1.setDescripcion("Balón oficial de la copa del mundo, alta durabilidad.");
            p1.setImageUrl("https://assets.adidas.com/images/w_600,f_auto,q_auto/4e6f49673966453fb6a3ae8c011e4062_9366/Pelota_Al_Rihla_League_Blanco_H57791_01_standard.jpg");

            // --- PELOTA 2 ---
            Pelota p2 = new Pelota();
            p2.setNombre("LeBron Playground");
            p2.setMarca("Nike");
            p2.setPrecio(new BigDecimal("32000.00")); 
            p2.setDeporte("Basketball");
            p2.setStock(5);
            p2.setDescripcion("Pelota de goma resistente para canchas exteriores.");
            p2.setImageUrl("https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/f0a4f5f5-0955-4654-9556-943063683648/pelota-de-baloncesto-lebron-playground-4p-8f6Kz5.png");

            pelotaRepository.save(p1);
            pelotaRepository.save(p2);

            System.out.println("✅ DATOS DE PRUEBA CARGADOS EN CATALOGO (CON BIGDECIMAL)");
        }
    }
}