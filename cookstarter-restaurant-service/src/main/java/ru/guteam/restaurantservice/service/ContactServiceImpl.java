package ru.guteam.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.guteam.restaurantservice.dto.ContactDTO;
import ru.guteam.restaurantservice.exception.ContactNotFoundException;
import ru.guteam.restaurantservice.model.Contact;
import ru.guteam.restaurantservice.repo.ContactRepo;
import ru.guteam.restaurantservice.util.Mapper;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepo contactRepo;
    private final Mapper mapper;

    @Override
    @Transactional
    public void saveContact(ContactDTO contact) {
        contactRepo.save(mapper.mapToContact(contact));
    }

    @Override
    public ContactDTO getContactByRestaurantId(Long restaurantId) {
        Contact contact = contactRepo.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new ContactNotFoundException(restaurantId));
        return mapper.mapToContactDTO(contact);
    }

    @Override
    @Transactional
    public void updateContact(ContactDTO contact) {
        Contact oldContact = contactRepo.findByRestaurantId(contact.getRestaurantId())
                .orElseThrow(() -> new ContactNotFoundException(contact.getRestaurantId()));
        Contact newContact = mapper.mapToContact(contact);
        newContact.setId(oldContact.getId());
        contactRepo.save(newContact);
    }

    @Override
    @Transactional
    public void deleteContactByRestaurantId(Long id) {
        contactRepo.deleteByRestaurantId(id);
    }
}
