import { makeStyles } from '@material-ui/core/styles';
import React from 'react';

function StarDestroyerTakingOff({ className }) {
  const useStyles = makeStyles(theme => ({
    root: {
      width: '1.5em',
    },
  }));

  const classes = useStyles();

  return <img className={`${classes.root} ${className}`} src={'/departure.png'} alt="Star destroyer taking off"/>;
}

function StarDestroyerLanding({ className }) {
  const useStyles = makeStyles(theme => ({
    root: {
      width: '1.5em',
    },
  }));

  const classes = useStyles();

  return <img className={`${classes.root} ${className}`} src={'/arrival.png'} alt="Star destroyer landing"/>;
}

function StarDestroyerTopView({ className }) {
  const useStyles = makeStyles(theme => ({
    root: {
      height: '1em',
      verticalAlign: 'baseline'
    },
  }));

  const classes = useStyles();

  return <img className={`${classes.root} ${className}`} src={'/star-destroyer.png'} alt="Star destroyer"/>;
}

export { StarDestroyerTakingOff, StarDestroyerLanding, StarDestroyerTopView };
