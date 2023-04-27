import React, { useState, useEffect } from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Button, Paper, Box } from "@material-ui/core";
import Typography from "@material-ui/core/Typography";
import moment from "moment";
import CardTravelIcon from "@material-ui/icons/CardTravel";
import EventIcon from "@material-ui/icons/Event";
import AccessTimeIcon from "@material-ui/icons/AccessTime";
import LanguageIcon from "@material-ui/icons/Language";
import { withRouter } from "react-router-dom";
import { proxiedUrl } from "../utils";
import Class from "../Commons/Class";

function Selection({ history, links }) {
  const [selection, setSelection] = useState();

  useEffect(() => {
    (async () => {
      if (links !== undefined) {
        const response = await fetch(proxiedUrl(links.selection.href));
        const result = await response.json();
        setSelection(result);
      }
    })();
  }, [links]);

  function book(link) {
    (async () => {
      const response = await fetch(proxiedUrl(link), {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        }
      });
      const booking = await response.json();
      history.push(`/bookings/${btoa(booking._links.self.href)}`);
    })();
  }

  const useStyles = makeStyles(theme => ({
    selection: {
      display: "flex",
      flexDirection: "column",
      justifyContent: "space-between",
      minHeight: "50%",
      padding: theme.spacing(2)
    },
    totalPrice: {
      marginTop: theme.spacing(3),
      marginBottom: theme.spacing(2),
      textAlign: "right"
    }
  }));
  const classes = useStyles();

  return (
    <Paper className={classes.selection}>
      <Title />
      {selection && (
        <>
          <div>
            {selection.spaceTrains.map(spacetrain => (
              <SpaceTrain key={spacetrain.number} spacetrain={spacetrain} />
            ))}
          </div>
          <div>
            {selection.totalPrice && (
              <Typography variant="h5" className={classes.totalPrice}>
                Total Price: {selection.totalPrice.amount}{" "}
                {selection.totalPrice.currency}
              </Typography>
            )}
            <Button
              variant="contained"
              color="primary"
              size="large"
              fullWidth
              disabled={selection._links["create-booking"] === undefined}
              onClick={() => book(selection._links["create-booking"].href)}
            >
              Book
            </Button>
          </div>
        </>
      )}
    </Paper>
  );
}

function SpaceTrain({ spacetrain }) {
  return (
    <Box mb={5}>
      <Bound spacetrain={spacetrain} />
      <Schedule schedule={spacetrain.departureSchedule} />
      <Fare fare={spacetrain.fare} />
    </Box>
  );
}

function Bound({ spacetrain }) {
  return (
    <Box display="flex" flexDirection="column" mb={2}>
      <SpacePort id={spacetrain.originId} />
      <SpacePort id={spacetrain.destinationId} />
    </Box>
  );
}

function SpacePort({ id }) {
  const [spacePort, setSpacePort] = useState();

  useEffect(() => {
    (async () => {
      const response = await fetch(proxiedUrl(id));
      const result = await response.json();
      setSpacePort(result);
    })();
  }, [id]);

  return (
    <Box display="flex" alignItems="center">
      {spacePort !== undefined && (
        <>
          <SpacePortIcon />
          <div>{spacePort.name} - {spacePort.location}</div>
        </>
      )}
    </Box>
  );
}

function SpacePortIcon() {
  const useStyles = makeStyles(theme => ({
    icon: {
      marginRight: theme.spacing(1)
    }
  }));
  const classes = useStyles();
  return <LanguageIcon fontSize="small" className={classes.icon} />;
}

function Schedule({ schedule }) {
  const useStyles = makeStyles(theme => ({
    schedule: {
      fontWeight: 600
    },
    icon: {
      marginRight: theme.spacing(1)
    }
  }));
  const classes = useStyles();

  return (
    <Box
      display="flex"
      justifyContent="space-between"
      mb={1}
      className={classes.schedule}
    >
      <Box key="date" display="flex" alignItems="center">
        <EventIcon fontSize="small" className={classes.icon} />
        <div>{moment(schedule).format("dddd, MMMM Do")}</div>
      </Box>
      <Box key="time" display="flex" alignItems="center">
        <AccessTimeIcon fontSize="small" className={classes.icon} />
        <div>{moment(schedule).format("LT")}</div>
      </Box>
    </Box>
  );
}

function Fare({ fare }) {
  const useStyles = makeStyles(theme => ({
    fare: {
      display: "flex",
      justifyContent: "space-between",
      marginBottom: theme.spacing(1)
    }
  }));
  const classes = useStyles();
  return (
    <div className={classes.fare}>
      <Class comfortClass={fare.comfortClass} />
      <Price price={fare.price} />
    </div>
  );
}

function Price({ price }) {
  const useStyles = makeStyles(theme => ({
    price: {}
  }));
  const classes = useStyles();
  return (
    <div className={classes.price}>
      {price.amount} {price.currency}
    </div>
  );
}

function Title() {
  const useStyles = makeStyles(theme => ({
    icon: {
      marginRight: theme.spacing(1)
    }
  }));
  const classes = useStyles();
  return (
    <Box display="flex" alignItems="center" mb={5}>
      <CardTravelIcon color="primary" className={classes.icon} />
      <Typography variant="h5" color="primary">
        Your Trip
      </Typography>
    </Box>
  );
}

export default withRouter(Selection);
