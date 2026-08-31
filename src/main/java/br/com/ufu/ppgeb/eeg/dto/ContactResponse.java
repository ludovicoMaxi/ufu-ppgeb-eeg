package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Response DTO for a contact.
 *
 * @param id the contact id
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
 * @param createdAt the creation auditing timestamp
 * @param createdBy the creation auditing user
 * @param updatedAt the last update auditing timestamp
 * @param updatedBy the last update auditing user
 */
@Builder
public record ContactResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("name")
    String name,
    @JsonProperty("active")
    Boolean active,
    @JsonProperty("objectId")
    Long objectId,
    @JsonProperty("objectType")
    Long objectType,
    @JsonProperty("phone")
    String phone,
    @JsonProperty("cellphone")
    String cellphone,
    @JsonProperty("whatsapp")
    String whatsapp,
    @JsonProperty("facebook")
    String facebook,
    @JsonProperty("instagram")
    String instagram,
    @JsonProperty("email")
    String email,
    @JsonProperty("main")
    Boolean main,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    @JsonProperty("createdAt")
    ZonedDateTime createdAt,
    @JsonProperty("createdBy")
    String createdBy,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    @JsonProperty("updatedAt")
    ZonedDateTime updatedAt,
    @JsonProperty("updatedBy")
    String updatedBy) {
}