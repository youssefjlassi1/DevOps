package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class SkierServicesImpl implements ISkierServices {

    private final ISkierRepository skierRepository;
    private final IPisteRepository pisteRepository;
    private final ICourseRepository courseRepository;
    private final IRegistrationRepository registrationRepository;
    private final ISubscriptionRepository subscriptionRepository;

    private static final Logger log = LoggerFactory.getLogger(SkierServicesImpl.class);

    @Override
    public List<Skier> retrieveAllSkiers() {
        log.info("Retrieving all skiers...");
        return skierRepository.findAll();
    }

    @Override
    public Skier addSkier(Skier skier) {
        log.info("Adding skier: {}", skier);
        if (skier.getSubscription() != null) {
            switch (skier.getSubscription().getTypeSub()) {
                case ANNUAL:
                    skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusYears(1));
                    break;
                case SEMESTRIEL:
                    skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(6));
                    break;
                case MONTHLY:
                    skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(1));
                    break;
                default:
                    log.warn("Unknown subscription type: {}", skier.getSubscription().getTypeSub());
            }
        }
        return skierRepository.save(skier);
    }

    @Override
    public Skier assignSkierToSubscription(Long numSkier, Long numSubscription) {
        log.info("Assigning skier with ID {} to subscription with ID {}", numSkier, numSubscription);
        Skier skier = skierRepository.findById(numSkier)
                .orElseThrow(() -> new IllegalArgumentException("Skier not found with id: " + numSkier));
        Subscription subscription = subscriptionRepository.findById(numSubscription)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found with id: " + numSubscription));
        skier.setSubscription(subscription);
        return skierRepository.save(skier);
    }

    @Override
    public Skier addSkierAndAssignToCourse(Skier skier, Long numCourse) {
        log.info("Adding skier and assigning to course ID: {}", numCourse);
        Skier savedSkier = skierRepository.save(skier);
        Course course = courseRepository.findById(numCourse)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + numCourse));
        Set<Registration> registrations = savedSkier.getRegistrations();
        if (registrations == null) {
            registrations = new HashSet<>();
        }
        for (Registration r : registrations) {
            r.setSkier(savedSkier);
            r.setCourse(course);
            registrationRepository.save(r);
        }
        savedSkier.setRegistrations(registrations);
        return skierRepository.save(savedSkier);
    }

    @Override
    public void removeSkier(Long numSkier) {
        log.info("Removing skier with ID: {}", numSkier);
        skierRepository.deleteById(numSkier);
    }

    @Override
    public Skier retrieveSkier(Long numSkier) {
        log.info("Retrieving skier with ID: {}", numSkier);
        return skierRepository.findById(numSkier)
                .orElseThrow(() -> new IllegalArgumentException("Skier not found with id: " + numSkier));
    }

    @Override
    public Skier assignSkierToPiste(Long numSkieur, Long numPiste) {
        log.info("Assigning skier with ID {} to piste with ID {}", numSkieur, numPiste);
        Skier skier = skierRepository.findById(numSkieur)
                .orElseThrow(() -> new IllegalArgumentException("Skier not found with id: " + numSkieur));
        Piste piste = pisteRepository.findById(numPiste)
                .orElseThrow(() -> new IllegalArgumentException("Piste not found with id: " + numPiste));
        Set<Piste> pistes = skier.getPistes();
        if (pistes == null) {
            pistes = new HashSet<>();
        }
        pistes.add(piste);
        skier.setPistes(pistes);
        return skierRepository.save(skier);
    }

    @Override
    public List<Skier> retrieveSkiersBySubscriptionType(TypeSubscription typeSubscription) {
        log.info("Retrieving skiers with subscription type: {}", typeSubscription);
        return skierRepository.findBySubscription_TypeSub(typeSubscription);
    }
}
