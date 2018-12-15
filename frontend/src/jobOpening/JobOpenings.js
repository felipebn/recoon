import gql from 'graphql-tag';
import { Query } from 'react-apollo';
import React, { Component } from 'react';
import Link from 'react-router-dom/Link';

class JobOpenings extends Component {
  render() {
    return (
      <div className="container is-fluid">
        <h1 className="title">Job Openings</h1>

        <div className="columns is-multiline is-mobile">
          {this.props.list.map(opening => <JobOpeningCard opening={opening}/>)}
        </div>
      </div>
    );
  }
}

class JobOpeningCard extends Component {
  render() {
    var opening = this.props.opening;

    return (
      
        <div className="column is-one-third">
          <div className="box recoon-card">
          <Link to={`/jobopening/${opening.id}`} className="no-style">
            <h1>{opening.name}</h1>
            <div className="field is-grouped is-grouped-multiline recoon-card-tags">
              <div className="control">
                <div className="tags has-addons">
                  <span className="tag is-dark">Applicants</span>
                  <span className="tag is-info">{opening.applicantCount}</span>
                </div>
              </div>
              <div className="control">
                <div className="tags has-addons">
                  <span className="tag is-dark">Responsible</span>
                  <span className="tag is-success">{opening.responsible}</span>
                </div>
              </div>
            </div>
          </Link>
          </div>
        </div>
    );
  }
}

const GET_JOBOPENINGS = gql`
  {
    jobOpenings{
      id,
      name,
      applicantCount,
      responsible
    }
  }
`;

const JobOpeningsQuery = () => (
  <Query query={GET_JOBOPENINGS}>
    {({ loading, error, data }) => {
      if (loading) return 'Loading...';
      if (error) return `Error! ${error.message}`;

      return (
        <JobOpenings list={data.jobOpenings}/>
      );
    }}
  </Query>
);

export default JobOpeningsQuery;
