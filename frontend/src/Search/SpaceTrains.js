import React, { useState, useEffect } from "react";
import { proxiedUrl } from "../utils";
import { Paper, Typography, Button } from "@material-ui/core";
import moment from "moment";
import { makeStyles } from "@material-ui/core/styles";
import { withRouter } from "react-router-dom";
import RepublicCredit from "../Currency/RepublicCredit";

function SpaceTrains({ history, link, onSelection }) {
  const [spacetrains, setSpaceTrains] = useState();

  const useStyles = makeStyles(theme => ({
    spacetrains: {}
  }));
  const classes = useStyles();

  useEffect(() => {
    (async () => {
      if (link !== undefined) {
        const response = await fetch(proxiedUrl(link));
        const result = (await response.json()).spaceTrains;
        setSpaceTrains(result);
      }
    })();
  }, [link]);

  function selectFare(fareLink) {
    (async () => {
      const response = await fetch(proxiedUrl(fareLink), {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        }
      });
      const selection = await response.json();
      if (
        selection._links["create-booking"] === undefined &&
        selection._links["inbounds-for-current-selection"] !== undefined
      ) {
        history.push(
          `/searches/${btoa(selection._links.search.href)}/bound/inbound`
        );
      } else {
        onSelection(selection._links);
      }
    })();
  }

  return (
    <div className={classes.spacetrains}>
      {spacetrains &&
        spacetrains.map(spacetrain => (
          <SpaceTrain key={spacetrain.number} spacetrain={spacetrain} selectFare={selectFare} />
        ))}
    </div>
  );
}

function Duration({ duration }) {
  return (
    <Typography color="textSecondary">
      {moment.duration(duration).humanize()}
    </Typography>
  );
}


function Schedule({ schedule }) {
  const useStyles = makeStyles(theme => ({
    schedule: {
      fontWeight: 600,
      marginRight: theme.spacing(1)
    }
  }));
  const classes = useStyles();
  return (
    <div className={classes.schedule}>
      {moment(schedule).format("MM/DD - LT")}
    </div>
  );
}

function Fare({ fare, selectFare }) {
  const useStyles = makeStyles(theme => ({
    fare: {
      "&:nth-child(1)": {
        marginBottom: theme.spacing(1)
      },
      "& .MuiButton-endIcon": {
        marginLeft: "2px"
      }
    },
  }));
  const classes = useStyles();
  return (
    <Button
      color="secondary"
      variant="contained"
      size="small"
      className={classes.fare}
      onClick={() => selectFare(fare._links.select.href)}
      endIcon={getCurrencySymbol(fare.price.currency)}
    >
      {fare.comfortClass} {fare.price.amount}
    </Button>
  );
}

function getCurrencySymbol(currency) {
  return <RepublicCredit />
}
function SpacePort({ id, schedule }) {
  const [spacePort, setSpacePort] = useState();

  useEffect(() => {
    (async () => {
      const response = await fetch(proxiedUrl(id));
      const result = await response.json();
      setSpacePort(result);
    })();
  }, [id]);

  const useStyles = makeStyles(theme => ({
    spaceport: {
      display: "flex",
      alignItems: "center"
    }
  }));
  const classes = useStyles();
  return (
    <div className={classes.spaceport}>
      <Schedule schedule={schedule} />
      {spacePort !== undefined && <div>{spacePort.name}</div>}
    </div>
  );
}

function SpaceTrain({ spacetrain, selectFare }) {
  const useStyles = makeStyles(theme => ({
    spacetrain: {
      marginBottom: theme.spacing(2),
      padding: theme.spacing(2),
      display: "flex",
      justifyContent: "space-between"
    },
    fare: {
      fontWeight: 600,
      "&:nth-child(1)": {
        marginBottom: theme.spacing(1)
      }
    },
    fullHeight: {
      display: "flex",
      justifyContent: "space-between",
      flexDirection: "column"
    },
    centered: {
      display: "flex",
      justifyContent: "center",
      flexDirection: "column"
    }
  }));
  const classes = useStyles();
  return (
    <Paper className={classes.spacetrain}>
      <div className={classes.fullHeight}>
        <SpacePort
          id={spacetrain.originId}
          schedule={spacetrain.departureSchedule}
        />
        <SpacePort
          id={spacetrain.destinationId}
          schedule={spacetrain.arrivalSchedule}
        />
      </div>
      <div className={classes.centered}>
        <Typography color="secondary" variant="subtitle2">
          {spacetrain.number}
        </Typography>
        <Duration duration={spacetrain.duration} />
      </div>
      <div className={classes.fullHeight}>
        {spacetrain.fares.map(fare => (
          <Fare key={fare.comfortClass} fare={fare} selectFare={selectFare} />
        ))}
      </div>
    </Paper>
  );
}

export default withRouter(SpaceTrains);
