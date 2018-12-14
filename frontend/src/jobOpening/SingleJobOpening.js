import gql from 'graphql-tag';
import { Query } from 'react-apollo';
import React, { Component } from 'react';

class SingleJobOpening extends Component {
  render() {
    var opening = this.props.opening;
    return (
      <div className="container">
        <div>
          <a class="button is-link is-outlined is-pulled-right">
            <span>Add Candidate</span>
          </a>
          <h1 className="title">{opening.name}</h1>
          <h4 className="subtitle">{opening.workflow.name}</h4>
        </div>
        <br/>
        <div className="columns">
          {opening.workflow.stages.map(workflowStage => <WorkflowStageColumn stage={workflowStage}/>)}
        </div>
      </div>
    );
  }
}

class WorkflowStageColumn extends Component {
  render() {
    var stage = this.props.stage;

    return (
      <div className="column recoon-stage-column">
        <div className="stage-top-border"></div>
        <div className="stage-contents">
          <h2 className="title is-size-5">{stage.name}</h2>
          {stage.candidates.map(c => this.renderApplicantCard(c))}     
        </div>
      </div>
    );
  }

  renderApplicantCard(candidate){
    return (
      <div className="card stage-card">
        <div className="card-content">
          <div className="media">
            <div className="media-left">
              <figure className="image is-48x48">
                <img src="https://ui-avatars.com/api/?background=0D8ABC&color=fff"/>
              </figure>
            </div>
            <div className="media-content">
              <p className="title is-5">{candidate.name}</p>
            </div>
          </div>      
        </div>
      </div>
    );
  }
}

const GET_JOBOPENING = gql`
  query singleJobOpening($joOpeningId: ID!){
    jobOpening(id: $joOpeningId){
      id,
      name,
      applicantCount,
      responsible,
      workflow{
        id,
        name,
        stages {
          id, name, order, candidates {
            id, name
          }
        }
      }
    }
  }
`;

const SingleJobOpeningQuery = (routeProps) => (
  <Query query={GET_JOBOPENING} variables={{joOpeningId: routeProps.match.params.id}}>
    {({ loading, error, data }) => {
      if (loading) return 'Loading...';
      if (error) return `Error! ${error.message}`;

      return (
        <SingleJobOpening opening={data.jobOpening}/>
      );
    }}
  </Query>
);

export default SingleJobOpeningQuery;
