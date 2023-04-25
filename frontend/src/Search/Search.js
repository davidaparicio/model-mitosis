import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { proxiedUrl } from "../utils";
import Grid from "@material-ui/core/Grid";
import { makeStyles } from "@material-ui/core/styles";
import SpaceTrains from "./SpaceTrains";
import { withRouter } from "react-router-dom";
import { Typography } from "@material-ui/core";

function Search() {
  const { searchId } = useParams();
  const [links, setLinks] = useState();

  const useStyles = makeStyles(theme => ({
    title: {
      color: 'white'
    },
    grid: {
      flexGrow: 1,
      paddingTop: theme.spacing(4)
    }
  }));
  const classes = useStyles();

  useEffect(() => {
    (async () => {
      const response = await fetch(proxiedUrl(atob(searchId)));
      const result = await response.json();
      setLinks(result._links);
    })();
  }, [searchId]);

  return (
    <Grid
      container
      justifyContent="center"
      alignContent="center"
      className={classes.grid}
      spacing={6}
    >
      {links && getBounds(links).map(bound => {
        return (
          <Grid item xs={5} key={bound}>
            <Typography variant="h6" className={classes.title}>
              {bound.toUpperCase()}
            </Typography>
            <SpaceTrains
              link={getLink(links, bound)}
              bound={bound}
            />
          </Grid>)
      })}
    </Grid>
  );
}

function getLink(links, bound) {
  if (bound === "outbound") {
    return links["all-outbounds"].href;
  } else {
    return links["all-inbounds"].href;
  }
}

function getBounds(links) {
  return Object.getOwnPropertyNames(links)
    .filter(link => link.includes('all-'))
    .map(link => link.substring(4, link.length - 1))
}

export default withRouter(Search);
