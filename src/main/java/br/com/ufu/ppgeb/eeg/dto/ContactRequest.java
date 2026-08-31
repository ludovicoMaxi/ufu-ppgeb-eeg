package br.com.ufu.ppgeb.eeg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a contact.
 *
 * @param name the contact name
 * @param active whether the contact is active
 * @param objectId the object id
 * @param objectType the object type
 * @param phone the phone
 * @param cellphone the cellphone
 * @param whatsapp the whatsapp
 * @param facebook the facebook
 * @param instagram the instagram
 * @param email the email
 * @param main whether the contact is the principal
 */
public record ContactRequest(
    @NotBlank
    @Size(max = 256)
    @JsonProperty("name")
    String name,
    @NotNull
    @JsonProperty("active")
    Boolean active,
    @NotNull
    @JsonProperty("objectId")
    Long objectId,
    @NotNull
    @JsonProperty("objectType")
    Long objectType,
    @Size(max = 20)
    @JsonProperty("phone")
    String phone,
    @Size(max = 20)
    @JsonProperty("cellphone")
    String cellphone,
    @Size(max = 20)
    @JsonProperty("whatsapp")
    String whatsapp,
    @Size(max = 200)
    @JsonProperty("facebook")
    String facebook,
    @Size(max = 200)
    @JsonProperty("instagram")
    String instagram,
    @Size(max = 100)
    @JsonProperty("email")
    String email,
    @JsonProperty("main")
    Boolean main) {
}