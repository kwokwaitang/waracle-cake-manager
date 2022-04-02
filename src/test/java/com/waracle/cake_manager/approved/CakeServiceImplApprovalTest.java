package com.waracle.cake_manager.approved;

import com.oneeyedmen.okeydoke.Approver;
import com.oneeyedmen.okeydoke.junit5.ApprovalsExtension;
import com.waracle.cake_manager.dto.NewCakeRequestDto;
import com.waracle.cake_manager.repository.CakeRepository;
import com.waracle.cake_manager.service.CakeServiceImpl;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.io.IOException;

@ExtendWith(ApprovalsExtension.class)
public class CakeServiceImplApprovalTest {

    @Mock
    ModelMapper modelMapper = new ModelMapper();

    @Mock
    HttpClient httpClient;

    @Mock
    CakeRepository cakeRepository;

    CakeServiceImpl serviceImplUnderTest;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        serviceImplUnderTest = new CakeServiceImpl(modelMapper, httpClient, cakeRepository);
    }

    @Test
    void getNewCakeRequest_withTitleDescImage_correctTitle(Approver approver) throws Exception {
        NewCakeRequestDto mewNewCakeRequest = serviceImplUnderTest.getNewCakeRequestDto(
                "abc",
                "def",
                "https://some-nice-cake.com/with-lots-of-cream.gif");

        whenApprovedItIs("abc", approver);
        approver.assertApproved(mewNewCakeRequest.getTitle());
    }

    @Test
    void getNewCakeRequest_withTitleDescImage_correctDescription(Approver approver) throws Exception {
        NewCakeRequestDto mewNewCakeRequest = serviceImplUnderTest.getNewCakeRequestDto(
                "abc",
                "def",
                "https://some-nice-cake.com/with-lots-of-cream.gif");

        whenApprovedItIs("def", approver);
        approver.assertApproved(mewNewCakeRequest.getDescription());
    }

    @Test
    void getNewCakeRequest_withTitleDescImage_correctImageUrl(Approver approver) throws Exception {
        NewCakeRequestDto mewNewCakeRequest = serviceImplUnderTest.getNewCakeRequestDto(
                "abc",
                "def",
                "https://some-nice-cake.com/with-lots-of-cream.gif");

        whenApprovedItIs("https://some-nice-cake.com/with-lots-of-cream.gif", approver);
        approver.assertApproved(mewNewCakeRequest.getImageUrl());
    }

    private void whenApprovedItIs(String valueOrNull, Approver approver) throws IOException {
        if (valueOrNull == null)
            approver.removeApproved();
        else
            approver.makeApproved(valueOrNull);
    }
}
