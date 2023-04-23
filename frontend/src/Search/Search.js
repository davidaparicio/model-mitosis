import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { proxiedUrl } from "../utils";
import Grid from "@material-ui/core/Grid";
import { makeStyles } from "@material-ui/core/styles";
import SpaceTrains from "./SpaceTrains";
import Selection from "./Selection";
import { withRouter } from "react-router-dom";
import { Typography } from "@material-ui/core";

function Search() {
  const { searchId, bound } = useParams();
  const [links, setLinks] = useState();

  const useStyles = makeStyles(theme => ({
    title: {
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
  }, [searchId, bound]);

  return (
    <Grid
      container
      justify="center"
      alignContent="center"
      className={classes.grid}
      spacing={2}
    >
      <Grid item xs={9}>
        <Typography variant="h6" className={classes.title}>
          Select your {bound} Space Train
        </Typography>
      </Grid>
      <Grid item xs={6}>
        {links && (
          <SpaceTrains
            link={getLink(links, bound)}
            bound={bound}
            onSelection={setLinks}
          />
        )}
      </Grid>
      <Grid item xs={3}>
        {links && <Selection links={links} />}
      </Grid>
    </Grid>
  );
}

function getLink(links, bound) {
  if (bound === "outbound") {
    return links["all-outbounds"].href;
  }

  if (links["inbounds-for-current-selection"] !== undefined) {
    return links["inbounds-for-current-selection"].href;
  }

  return undefined;
}

export default withRouter(Search);
