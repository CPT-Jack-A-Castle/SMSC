package io.smsc.repository.customer;

import io.smsc.AbstractSpringMVCTest;
import io.smsc.model.customer.Contact;
import io.smsc.model.customer.Customer;
import io.smsc.model.customer.Salutation;
import io.smsc.model.customer.Type;
import org.junit.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(username = "Admin", roles = {"POWER_ADMIN_USER"})
public class ContactRestTest extends AbstractSpringMVCTest {

    @Test
    public void testGetSingleContact() throws Exception {
        mockMvc.perform(get("/rest/repository/customer-contacts/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.firstname", is("SMSC")))
                .andExpect(jsonPath("$.surname", is("SMSC")))
                .andExpect(jsonPath("$.phone", is("0674329568")))
                .andExpect(jsonPath("$.mobilePhone", is("0504569753")))
                .andExpect(jsonPath("$.fax", is("fake_fax")))
                .andExpect(jsonPath("$.emailAddress", is("smsc@bulk.io")))
                .andExpect(jsonPath("$.type", is(Type.CEO.toString())))
                .andExpect(jsonPath("$.salutation", is(Salutation.MR.toString())))
                .andDo(document("getContact",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(getPathParam("Contact")),
                        responseFields(contactFieldsForResponse(false))));
    }

    @Test
    public void testContactNotFound() throws Exception {
        mockMvc.perform(get("/rest/repository/customer-contacts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllContacts() throws Exception {
        mockMvc.perform(get("/rest/repository/customer-contacts?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.customer-contacts", hasSize(2)))
                .andExpect(jsonPath("$._embedded.customer-contacts[1].firstname", is("SMSC")))
                .andExpect(jsonPath("$._embedded.customer-contacts[1].surname", is("SMSC")))
                .andExpect(jsonPath("$._embedded.customer-contacts[1].phone", is("0674329568")))
                .andExpect(jsonPath("$._embedded.customer-contacts[1].mobilePhone", is("0504569753")))
                .andExpect(jsonPath("$._embedded.customer-contacts[1].fax", is("fake_fax")))
                .andExpect(jsonPath("$._embedded.customer-contacts[1].emailAddress", is("smsc@bulk.io")))
                .andExpect(jsonPath("$._embedded.customer-contacts[1].type", is(Type.CEO.toString())))
                .andExpect(jsonPath("$._embedded.customer-contacts[1].salutation", is(Salutation.MR.toString())))
                .andExpect(jsonPath("$._embedded.customer-contacts[0].firstname", is("Default first name")))
                .andExpect(jsonPath("$._embedded.customer-contacts[0].surname", is("Default surname")))
                .andExpect(jsonPath("$._embedded.customer-contacts[0].phone", is("0671234567")))
                .andExpect(jsonPath("$._embedded.customer-contacts[0].mobilePhone", is("0501234567")))
                .andExpect(jsonPath("$._embedded.customer-contacts[0].fax", is("default fax")))
                .andExpect(jsonPath("$._embedded.customer-contacts[0].emailAddress", is("default@gmail.com")))
                .andExpect(jsonPath("$._embedded.customer-contacts[0].type", is(Type.CEO.toString())))
                .andExpect(jsonPath("$._embedded.customer-contacts[0].salutation", is(Salutation.MRS.toString())))
                .andDo(document("getContacts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("Page of results"),
                                parameterWithName("size").description("Size of results")),
                        responseFields(contactFieldsForResponse(true))));
    }

    @Test
    public void testCreateContact() throws Exception {
        Contact contact = new Contact();
        contact.setId(null);
        contact.setFirstname("SMSC");
        contact.setSurname("SMSC");
        contact.setPhone("0322222222");
        contact.setMobilePhone("0632222222");
        contact.setFax("new_fake_fax");
        contact.setEmailAddress("new_fake@gmail.com");
        contact.setType(Type.PRIMARY);
        contact.setSalutation(Salutation.MRS);
        String customerContactJson = json(contact);
        // json is ignoring inserting customer through setter
        customerContactJson = customerContactJson.substring(0, customerContactJson.length() - 1).concat(", \"customer\" : \"/rest/repository/customers/40000\" \r\n }");

        mockMvc.perform(post("/rest/repository/customer-contacts")
                .with(csrf())
                .contentType("application/json;charset=UTF-8")
                .content(customerContactJson))
                .andExpect(status().isCreated())
                .andDo(document("createContact",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(contactFieldsForRequest()),
                        responseFields(contactFieldsForResponse(false))));
    }

    @Test
    public void testDeleteContact() throws Exception {
        mockMvc.perform(delete("/rest/repository/customer-contacts/{id}", 1)
                .with(csrf()))
                .andDo(document("deleteContact",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(getPathParam("Contact"))));

        mockMvc.perform(get("/rest/repository/customer-contacts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateContact() throws Exception {
        mockMvc.perform(patch("/rest/repository/customer-contacts/{id}", 2)
                .with(csrf())
                .contentType("application/json;charset=UTF-8")
                .content("{ \"emailAddress\" : \"fake@gmail.com\" }"))
                .andExpect(status().isOk())
                .andDo(document("updateContact",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(getPathParam("Contact")),
                        requestFields(contactFieldsForRequest()),
                        responseFields(contactFieldsForResponse(false))));

        mockMvc.perform(get("/rest/repository/customer-contacts/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailAddress", is("fake@gmail.com")));
    }

    @Test
    public void testReplaceContact() throws Exception {
        Contact contact = new Contact();
        contact.setId(2L);
        contact.setFirstname("SMSC");
        contact.setSurname("SMSC");
        contact.setPhone("0322222222");
        contact.setMobilePhone("0632222222");
        contact.setFax("new_fake_fax");
        contact.setEmailAddress("fake@gmail.com");
        contact.setType(Type.PRIMARY);
        contact.setSalutation(Salutation.MRS);
        String customerContactJson = json(contact);

        mockMvc.perform(put("/rest/repository/customer-contacts/{id}", 2)
                .with(csrf())
                .contentType("application/json;charset=UTF-8")
                .content(customerContactJson))
                .andExpect(status().isOk())
                .andDo(document("replaceContact",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(getPathParam("Contact")),
                        requestFields(contactFieldsForRequest()),
                        responseFields(contactFieldsForResponse(false))));

        mockMvc.perform(get("/rest/repository/customer-contacts/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.firstname", is("SMSC")))
                .andExpect(jsonPath("$.surname", is("SMSC")))
                .andExpect(jsonPath("$.phone", is("0322222222")))
                .andExpect(jsonPath("$.mobilePhone", is("0632222222")))
                .andExpect(jsonPath("$.fax", is("new_fake_fax")))
                .andExpect(jsonPath("$.emailAddress", is("fake@gmail.com")))
                .andExpect(jsonPath("$.type", is(Type.PRIMARY.toString())))
                .andExpect(jsonPath("$.salutation", is(Salutation.MRS.toString())));
    }

    /**
     * Contact fields used in responses.
     * An array field equivalent can be provided
     *
     * @param isJsonArray if the fields are used in a JsonArray
     * @return FieldDescriptor
     */
    private FieldDescriptor[] contactFieldsForResponse(boolean isJsonArray) {
        return isJsonArray ?
                new FieldDescriptor[]{
                        fieldWithPath("_embedded.customer-contacts[]").description("Contact list"),
                        fieldWithPath("_embedded.customer-contacts[].id").description("Contact's id"),
                        fieldWithPath("_embedded.customer-contacts[].firstname").description("Contact's firstname"),
                        fieldWithPath("_embedded.customer-contacts[].surname").description("Contact's surname"),
                        fieldWithPath("_embedded.customer-contacts[].phone").description("Contact's phone"),
                        fieldWithPath("_embedded.customer-contacts[].mobilePhone").description("Contact's mobilePhone"),
                        fieldWithPath("_embedded.customer-contacts[].fax").description("Contact's fax"),
                        fieldWithPath("_embedded.customer-contacts[].emailAddress").description("Contact's emailAddress"),
                        fieldWithPath("_embedded.customer-contacts[].type").type(Type.class).description("Contact's type"),
                        fieldWithPath("_embedded.customer-contacts[].salutation").type(Salutation.class).description("Contact's salutation"),
                        fieldWithPath("_embedded.customer-contacts[].lastModifiedDate").type(Date.class).description("Contact's date of last modification"),
                        fieldWithPath("_links").optional().ignored(),
                        fieldWithPath("page").optional().ignored()
                } :
                new FieldDescriptor[]{
                        fieldWithPath("id").description("Contact's id"),
                        fieldWithPath("firstname").description("Contact's firstname"),
                        fieldWithPath("surname").description("Contact's surname"),
                        fieldWithPath("phone").description("Contact's phone"),
                        fieldWithPath("mobilePhone").description("Contact's mobilePhone"),
                        fieldWithPath("fax").description("Contact's fax"),
                        fieldWithPath("emailAddress").description("Contact's emailAddress"),
                        fieldWithPath("type").type(Type.class).description("Contact's type"),
                        fieldWithPath("salutation").type(Salutation.class).description("Contact's salutation"),
                        fieldWithPath("lastModifiedDate").type(Date.class).description("Contact's date of last modification"),
                        fieldWithPath("_links").optional().ignored(),
                        fieldWithPath("page").optional().ignored()
                };
    }

    /**
     * Contact fields used in requests.
     *
     * @return FieldDescriptor
     */
    private FieldDescriptor[] contactFieldsForRequest() {
        return new FieldDescriptor[]{
                fieldWithPath("firstname").optional().type(String.class).description("Contact's firstname"),
                fieldWithPath("surname").optional().type(String.class).description("Contact's surname"),
                fieldWithPath("phone").optional().type(String.class).description("Contact's phone"),
                fieldWithPath("mobilePhone").optional().type(String.class).description("Contact's mobilePhone"),
                fieldWithPath("fax").optional().type(String.class).description("Contact's fax"),
                fieldWithPath("emailAddress").optional().type(String.class).description("Contact's emailAddress"),
                fieldWithPath("type").optional().type(Type.class).description("Contact's type"),
                fieldWithPath("salutation").optional().type(Salutation.class).description("Contact's salutation"),
                fieldWithPath("customer").optional().type(Customer.class).description("Contact's customer"),
                fieldWithPath("id").optional().ignored(),
                fieldWithPath("lastModifiedDate").optional().ignored(),
                fieldWithPath("_links").optional().ignored(),
                fieldWithPath("page").optional().ignored()
        };
    }
}
