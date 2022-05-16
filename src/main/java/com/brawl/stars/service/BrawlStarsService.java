package com.brawl.stars.service;

import com.brawl.stars.dao.BrawlStarsDao;
import com.brawl.stars.entity.Action;
import com.brawl.stars.entity.Personage;
import com.brawl.stars.entity.Rare;
import com.brawl.stars.entity.Specification;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatformException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

@NoArgsConstructor
public class BrawlStarsService {

    private BrawlStarsDao dao;
    final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BrawlStarsService brawlStarsService = new BrawlStarsService();
        brawlStarsService.init();
    }

    public void init() throws IOException {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("brawlStars");
        EntityManager em = factory.createEntityManager();
        dao = new BrawlStarsDao(em);
        menu();
    }

    private void menu() throws IOException {
        try {
            while (true) {
                System.out.println("""
                        What you want?
                        1) Create new personage
                        2) Update personage by name
                        3) Select personage by name
                        4) Select all personage""");
                switch (scanner.nextInt()) {
                    case 1:
                        create();
                        break;
                    case 2:
                        update();
                        break;
                    case 3:
                        selectByName();
                        break;
                    case 4:
                        selectAll();
                        break;
                    default:
                        break;
                }
                System.out.println("\n\n");
            }
        } catch (Exception e) {
            //ignored
        }
    }

    private void create() throws Exception {
        Personage personage = new Personage();
        Specification specification = new Specification();
        System.out.println("Write name:");
        personage.setName(scanner.next());
        try {
            System.out.println("Write rare:");
            personage.setRare(Rare.valueOf(scanner.next().toUpperCase()));
        } catch (Exception e) {
            System.out.println("Invalid rare value");
            throw new Exception();
        }
        try {
            System.out.println("Write health:");
            specification.setHealth(Integer.parseInt(scanner.next()));
        } catch (Exception e) {
            System.out.println("Invalid damage value");
            throw new Exception();
        }
        System.out.println("Write speed:");
        specification.setSpeed(scanner.next());
        System.out.println("Write damage:");
        final String damage = reader.readLine().trim();
        if (damage.matches("^\\d+\\sx\\s\\d+$") || damage.matches("\\d+")) {
            specification.setDamage(damage);
        } else {
            System.out.println("Invalid damage value");
            throw new Exception();
        }
        System.out.println("Write rang:");
        specification.setRang(scanner.next());
        System.out.println("Write rechargeRate:");
        specification.setRechargeRate(scanner.next());
        personage.setSpecification(specification);
        personage.setId(UUID.randomUUID());
        specification.setId(personage.getId());
        dao.savePersonage(personage);
    }

    private void update() throws Exception {
        try {
            System.out.println("Write personage name:");
            final String name = scanner.next();
            System.out.println("You want increase(1) or decrease(2) personage?");
            final int num = scanner.nextInt();
            if (num == 1) updatePersonage(name, Action.INCREASE);
            else if (num == 2) updatePersonage(name, Action.DECREASE);
        } catch (JtaPlatformException e) {
            System.out.println(e.getMessage());
            throw new Exception();
        }
    }

    private void selectByName() {
        try {
            System.out.println("Write personage name:");
            final String name = scanner.next().trim();
            System.out.println(dao.getPersonageByName(name, Personage.class));
        } catch (JtaPlatformException e) {
            System.out.println(e.getMessage());
        }
    }

    private void selectAll() {
        try {
            dao.getAllPersonage(Personage.class).stream()
                    .map(Objects::toString)
                    .forEach(System.out::println);
        } catch (JtaPlatformException e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    private void updatePersonage(final String name ,final Action action) {
        final Personage personage = dao.getPersonageByName(name, Personage.class);
        final Specification specification = personage.getSpecification();
        final String[] damageArr = specification.getDamage().split(StringUtils.SPACE);
        final StringBuilder builder;
        final int damage;
        if (damageArr.length == 1) {
            builder = new StringBuilder();
            damage = Integer.parseInt(damageArr[0]);
        } else {
            damage = Integer.parseInt(damageArr[2]);
            builder = new StringBuilder()
                    .append(damageArr[0])
                    .append(StringUtils.SPACE)
                    .append(damageArr[1])
                    .append(StringUtils.SPACE);
        }
        if (action == Action.INCREASE) {
            specification.setHealth((int)(specification.getHealth() * 1.1));
            builder.append((int)(damage * 1.1));
        } else {
            specification.setHealth((int)(specification.getHealth() * 0.9));
            builder.append((int)(damage * 0.9));
        }
        specification.setDamage(builder.toString());
        dao.updatePersonage(personage);
    }

}
