import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { proxiedUrl } from "../utils";
import Grid from "@material-ui/core/Grid";
import { withRouter } from "react-router-dom";
import FlightTakeoffIcon from "@material-ui/icons/FlightTakeoff";
import { Paper, Typography, Box } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import FlightLandIcon from "@material-ui/icons/FlightLand";
import moment from "moment";
import ExploreIcon from "@material-ui/icons/Explore";
import Class from "../Commons/Class";
import FlightIcon from "@material-ui/icons/Flight";
import { getCurrencySymbol } from "../Commons/Currency";

function Booking({ history }) {
  const { bookingId } = useParams();
  const [booking, setBooking] = useState();

  const useStyles = makeStyles(theme => ({
    grid: {
      flexGrow: 1,
      paddingTop: theme.spacing(4)
    },
    booking: {
      padding: theme.spacing(3)
    },
    totalPrice: {
      textAlign: "right",
      marginBottom: theme.spacing(2)
    }
  }));
  const classes = useStyles();

  useEffect(() => {
    (async () => {
      const response = await fetch(proxiedUrl(atob(bookingId)));
      const result = await response.json();
      setBooking(result);
    })();
  }, [bookingId]);

  return (
    <Grid
      container
      justifyContent="center"
      alignContent="center"
      className={classes.grid}
      spacing={2}
    >
      <Grid item xs={6}>
        {booking && (
          <Paper className={classes.booking}>
            <Title />
            {booking.spaceTrains.map(spacetrain => (
              <SpaceTrain key={spacetrain.number} spacetrain={spacetrain} />
            ))}
            <Typography variant="h6" className={classes.totalPrice}>
              Total Price: {booking.totalPrice.amount}{getCurrencySymbol(booking.totalPrice.currency)}
            </Typography>

            <Button
              size="large"
              color="secondary"
              variant="contained"
              fullWidth
              onClick={() => history.push("/")}
            >
              Back
            </Button>
          </Paper>
        )}
      </Grid>
    </Grid>
  );
}

function Bound({ spacePortId, schedule, arrival }) {
  const useStyles = makeStyles(theme => ({
    icon: {
      marginRight: theme.spacing(1)
    }
  }));
  const classes = useStyles();

  const [spacePort, setSpacePort] = useState();

  useEffect(() => {
    (async () => {
      const response = await fetch(proxiedUrl(spacePortId));
      const result = await response.json();
      setSpacePort(result);
    })();
  }, [spacePortId]);

  return (
    <Box display="flex" alignItems="center" mb={1}>
      {arrival === true ? (
        <FlightLandIcon className={classes.icon} />
      ) : (
        <FlightTakeoffIcon className={classes.icon} />
      )}
      <Box display="flex" alignItems="baseline">
        {spacePort !== undefined && (
          <div>
            {spacePort.name} ({spacePort.location})
          </div>
        )}
        <div>&nbsp;on&nbsp;</div>
        <Typography color="primary" variant="subtitle2">
          {moment(schedule).format("LLLL")}
        </Typography>
      </Box>
    </Box>
  );
}

function SpaceTrain({ spacetrain }) {
  return (
    <Box mb={5} border={1} borderRadius={4} p={2} borderColor="primary.main">
      <Bound
        key="outbound-departure"
        spacePortId={spacetrain.originId}
        schedule={spacetrain.departureSchedule}
      />
      <Bound
        key="outbound-arrival"
        spacePortId={spacetrain.destinationId}
        schedule={spacetrain.arrivalSchedule}
        arrival
      />
      <Description number={spacetrain.number} fare={spacetrain.fare} />
    </Box>
  );
}

function Description({ number, fare }) {
  const useStyles = makeStyles(theme => ({
    marginRight: {
      marginRight: theme.spacing(1)
    }
  }));
  const classes = useStyles();
  return (
    <Box display="flex" alignItems="flex-end" justifyContent="space-between">
      <Box display="flex" alignItems="baseline">
        <FlightIcon className={classes.marginRight} />
        <Typography
          className={classes.marginRight}
          color="primary"
          variant="subtitle2"
        >
          {number}
        </Typography>
        <Class
          comfortClass={fare.comfortClass}
          className={classes.marginRight}
        />
      </Box>
      <Box display="flex" flexDirection="column" alignItems="flex-end">
        {fare.discount && <>
          <Price basePrice price={fare.basePrice} />
          <Discount discount={fare.discount} />
        </>}
        <Price price={fare.price} />
      </Box>
    </Box>
  );
}
function Discount({ discount }) {
  return (
    <div>
      <Typography variant="body2" color="secondary">
        {-discount.amount}{getCurrencySymbol(discount.currency)}
      </Typography>
    </div>
  );
}

function Price({ basePrice, price }) {
  const useStyles = makeStyles(theme => ({
    basePrice: {
      textDecoration: "line-through"
    },
    price: {
      fontWeight: "600"
    }
  }));
  const classes = useStyles();
  return (
    <div>
      <Typography className={basePrice ? classes.basePrice : classes.price} variant={basePrice ? "body2" : "body1"}>
        {!basePrice && "Price: "}{price.amount}{getCurrencySymbol(price.currency)}
      </Typography>
    </div>
  );
}

function Title() {
  const useStyles = makeStyles(theme => ({
    title: {
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      marginBottom: theme.spacing(5)
    },
    icon: {
      marginRight: theme.spacing(1)
    }
  }));
  const classes = useStyles();

  return (
    <div className={classes.title}>
      <ExploreIcon className={classes.icon} color="primary" />
      <Typography variant="h5">
        Ready to take-off? Your trip is booked!
      </Typography>
    </div>
  );
}
export default withRouter(Booking);
