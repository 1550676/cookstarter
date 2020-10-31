package ru.guteam.restaurantservice.service;

import ru.guteam.restaurantservice.dto.ContactDTO;
import ru.guteam.restaurantservice.model.Contact;

public interface ContactService {
    void saveContact(ContactDTO contact);

    ContactDTO getContactByRestaurantId(Long restaurant_id);

    void updateContact(Long restaurant_id, ContactDTO contact);

    void deleteContactByRestaurantId(Long restaurant_id);

}
