import React, {useEffect, useState} from "react";
import {proxiedUrl} from "../utils";
import {Box, Button, Paper, Typography} from "@material-ui/core";
import moment from "moment";
import {makeStyles} from "@material-ui/core/styles";
import {useParams, withRouter} from "react-router-dom";
import SeatLocation from "./SeatLocation";
import Grid from "@material-ui/core/Grid";
import FlightIcon from "@material-ui/icons/Flight";
import LanguageIcon from "@material-ui/icons/Language";
import EventIcon from "@material-ui/icons/Event";
import AccessTimeIcon from "@material-ui/icons/AccessTime";
import Class from "../Commons/Class";

function SelectSeatLocation({history}) {
    const {bookingId, spaceTrainNumber} = useParams();
    const [spaceTrain, setSpaceTrain] = useState();

    useEffect(() => {
        (async () => {
            const response = await fetch(proxiedUrl(atob(bookingId)));
            const result = await response.json();

            setSpaceTrain(result.spaceTrains.find(
                spaceTrain => {
                    return spaceTrain.number === spaceTrainNumber
                }
            ));
        })();
    }, [bookingId]);


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

    const seatLocations = [SeatLocation.ANY, SeatLocation.SHARED_CABIN, SeatLocation.TORPEDO_STORAGE_COMPARTMENT, SeatLocation.CARGO_BAY];

    return (
        <Grid
            container
            justify="center"
            alignContent="center"
            className={classes.grid}
        >
            <Grid item xs={9}>
                <Typography variant="h6" className={classes.title}>
                    Select your Seat location
                </Typography>
            </Grid>
            <Grid container
                  alignContent="center"
                  item xs={9}
                  spacing={2}>


                <Grid item xs={9}
                      container
                      spacing={2}
                      alignContent="stretch"
                >
                    {seatLocations &&
                        seatLocations.map(seatLocations => (
                            <Grid item xs={3}>
                                <SeatLocationGridItem seatLocation={seatLocations}/>
                            </Grid>
                        ))}
                </Grid>
                <Grid item xs={3}>
                    {spaceTrain && <SpaceTrainInfo spacetrain={spaceTrain}/>}
                </Grid>
            </Grid>
        </Grid>
    );
}

function SeatLocationGridItem({seatLocation}) {
    const useStyles = makeStyles(theme => ({
        seatLocation: {
            marginBottom: theme.spacing(2),
            padding: theme.spacing(2),
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-between",
        },
        illustration: {
            '& img': {
                width: '100%'
            }
        },
        centered: {
            justifyContent: "center"
        }
    }));
    const classes = useStyles();
    return (
        <Paper className={classes.seatLocation}>
            <Box className={classes.illustration} mb={2}>
                <img src={seatLocation.illustration}/>
            </Box>
            <Box mb={2}>
                <Typography color="primary" variant="subtitle2">
                    {seatLocation.label}
                </Typography>
            </Box>

            <SelectionButton/>
        </Paper>
    );
}

function SelectionButton() {
    const useStyles = makeStyles(theme => ({
        fare: {
            textAlign: "right",
            "&:nth-child(1)": {
                marginBottom: theme.spacing(1.5)
            }
        },
        price: {
            width: "100%",
            marginBottom: theme.spacing(0.5),
            fontWeight: "800"

        }
    }));
    const classes = useStyles();
    return (
        <div className={classes.fare}>
            <Button
                color="secondary"
                variant="contained"
                size="small"
                className={classes.price}
            >
                Select Seat Location
            </Button>
        </div>
    );
}

function SpaceTrainInfo({spacetrain}) {
    const useStyles = makeStyles(theme => ({
        spaceTrainInfo: {
            padding: theme.spacing(1)
        }
    }));
    const classes = useStyles();

    return (
        <Paper className={classes.spaceTrainInfo}>
            <SpaceTrainInfoTitle spacetrain={spacetrain}/>
            <SpaceTrain spacetrain={spacetrain}/>
        </Paper>
    );
}

function SpaceTrainInfoTitle({spacetrain}) {
    const useStyles = makeStyles(theme => ({
        icon: {
            marginRight: theme.spacing(1)
        }
    }));
    const classes = useStyles();

    return (
        <Box display="flex" alignItems="center" mb={2}>
            <FlightIcon color="primary" className={classes.icon}/>
            <Typography variant="h5" color="primary">
                {spacetrain.number}
            </Typography>
        </Box>
    );
}

function SpaceTrain({spacetrain}) {
    return (
        <Box>
            <Bound spacetrain={spacetrain}/>
            <Schedule schedule={spacetrain.departureSchedule}/>
            <Fare fare={spacetrain.fare}/>
        </Box>
    );
}

function Bound({spacetrain}) {
    return (
        <Box display="flex" flexDirection="column" mb={2}>
            <SpacePort id={spacetrain.originId}/>
            <SpacePort id={spacetrain.destinationId}/>
        </Box>
    );
}

function SpacePort({id}) {
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
                    <SpacePortIcon/>
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
    return <LanguageIcon fontSize="small" className={classes.icon}/>;
}

function Schedule({schedule}) {
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
                <EventIcon fontSize="small" className={classes.icon}/>
                <div>{moment(schedule).format("dddd, MMMM Do")}</div>
            </Box>
            <Box key="time" display="flex" alignItems="center">
                <AccessTimeIcon fontSize="small" className={classes.icon}/>
                <div>{moment(schedule).format("LT")}</div>
            </Box>
        </Box>
    );
}

function Fare({fare}) {
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
            <Class comfortClass={fare.comfortClass}/>
        </div>
    );
}

export default withRouter(SelectSeatLocation);
