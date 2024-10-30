package com.well_sync.logic;

import com.well_sync.logic.exceptions.InvalidDoctorException;
import com.well_sync.objects.Doctor;


/**
 * Package-private utility class to validate fields in the Doctor DSO.
 */
abstract class DoctorValidator {
    public static void validateDoctor(Doctor doctor) throws InvalidDoctorException {
        if (doctor == null)
            throw new InvalidDoctorException("Doctor details object undefined.");

        ValidationUtils.validateEmail(InvalidDoctorException.class, doctor.getEmail());
        ValidationUtils.validateName(InvalidDoctorException.class, doctor.getFirstName());
        ValidationUtils.validateName(InvalidDoctorException.class, doctor.getLastName());
    }
}
