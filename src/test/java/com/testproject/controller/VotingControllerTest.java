package com.testproject.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.ImmutableMap;
import com.testproject.domain.PointName;
import com.testproject.domain.Voting;
import com.testproject.service.VotingService;
import com.testproject.util.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@WebMvcTest({VotingController.class, Converter.class})
public class VotingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private VotingService service;

  @Spy
  private Voting voting = new Voting();

  @Before
  public void setUp() {
    final Integer expectedId = 1;
    final String expectedTitle = "Title";
    final boolean expectedIsActive = true;

    Map<PointName, Integer> votes =
        ImmutableMap.of(
            PointName.YES, 5,
            PointName.NO, 4,
            PointName.PROBABLY, 3
        );

    voting.setId(expectedId);
    voting.setTitle(expectedTitle);
    voting.setActive(expectedIsActive);
    voting.setVotes(votes);
  }

  @Test
  public void shouldReturnVotingById() throws Exception {
    when(service.getVoting(any(Integer.class))).thenReturn(voting);

    MvcResult result = mockMvc
        .perform(
            get("/votings/1")
                .accept(
                    MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    Voting actualVoting = deserializeVotingFromJson(content);

    assertVoting(actualVoting);
    assertEquals(voting.isActive(), actualVoting.isActive());

    verify(service).getVoting(voting.getId());
    verifyNoMoreInteractions(service);
  }

  @Test
  public void shouldReturnExceptionWhenVotingById() throws Exception {
    when(service.getVoting(any(Integer.class))).thenThrow(NoSuchElementException.class);

    mockMvc
        .perform(
            get("/votings/1")
                .accept(
                    MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound()).andReturn();

    verify(service).getVoting(voting.getId());
  }

  @Test
  public void shouldVoteYes() throws Exception {
    Map<PointName, Integer> votes =
        new HashMap<PointName, Integer>() {
          {
            put(PointName.YES, 0);
            put(PointName.NO, 0);
            put(PointName.PROBABLY, 0);
          }
        };
    voting.setVotes(votes);

    when(service.getVoting(any(Integer.class))).thenReturn(voting);
    when(service.updateVoting(any(Voting.class))).thenReturn(voting);

    MvcResult result = mockMvc
        .perform(
            patch("/votings/1/vote")
                .content("{\"pointName\":\"YES\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(
                    MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    Voting actualVoting = deserializeVotingFromJson(content);

    assertVoting(actualVoting);
    assertEquals(voting.isActive(), actualVoting.isActive());

    verify(service).getVoting(this.voting.getId());
    verify(service).updateVoting(this.voting);
    verifyNoMoreInteractions(service);
  }

  @Test
  public void shouldReturnVotingPointScore() throws Exception {
    when(service.getVoting(any(Integer.class))).thenReturn(voting);

    MvcResult result = mockMvc
        .perform(
            get("/votings/1/vote/YES")
                .accept(
                    MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    assertEquals("{\"pointName\":\"YES\",\"count\":5}", content);

    verify(service).getVoting(voting.getId());
    verifyNoMoreInteractions(service);
  }

  @Test
  public void shouldStopVoting() throws Exception {
    when(service.getVoting(any(Integer.class))).thenReturn(voting);
    when(service.updateVoting(any(Voting.class))).thenReturn(voting);
    when(voting.isActive()).thenReturn(true).thenReturn(false);

    MvcResult result = mockMvc
        .perform(
            patch("/votings/1/stop")
                .accept(
                    MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    Voting actualVoting = deserializeVotingFromJson(content);

    assertVoting(actualVoting);
    assertEquals(false, actualVoting.isActive());

    verify(service).getVoting(voting.getId());
    verify(service).updateVoting(voting);
    verifyNoMoreInteractions(service);
  }

  private void assertVoting(final Voting actualVoting) {
    assertEquals(voting.getId(), actualVoting.getId());
    assertEquals(voting.getTitle(), actualVoting.getTitle());
    assertEquals(voting.getVotes(), actualVoting.getVotes());
  }

  private Voting deserializeVotingFromJson(String json) throws IOException {
    return new ObjectMapper().readValue(json, Voting.class);
  }
}
