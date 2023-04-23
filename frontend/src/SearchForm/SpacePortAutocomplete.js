import React, { useState, useEffect } from 'react'
import TextField from '@material-ui/core/TextField'
import Autocomplete from '@material-ui/lab/Autocomplete'
import CircularProgress from '@material-ui/core/CircularProgress'
import FlightTakeoffIcon from '@material-ui/icons/FlightTakeoff'
import fetch from 'cross-fetch'
import { makeStyles } from '@material-ui/core/styles'
import FlightLandIcon from '@material-ui/icons/FlightLand'

function SpacePortAutocomplete ({ id, arrival, onSelect }) {
  const [open, setOpen] = useState(false)
  const [options, setOptions] = useState([])
  const loading = open && options.length === 0

  const useStyles = makeStyles(theme => ({
    root: {
      marginBottom: theme.spacing(1)
    },
    placeholder: {
      display: 'flex',
      alignItems: 'center'
    },
    icon: {
      marginRight: theme.spacing(1)
    },
    bound: {
      textTransform: 'capitalize'
    }
  }))

  const classes = useStyles()

  useEffect(() => {
    let active = true

    if (!loading) {
      return undefined
    }

    ;(async () => {
      const response = await fetch('/spaceports')
      const spaceports = (await response.json())._embedded.spacePorts

      if (active) {
        setOptions(Object.keys(spaceports).map(key => spaceports[key]))
      }
    })()

    return () => {
      active = false
    }
  }, [loading])

  useEffect(() => {
    if (!open) {
      setOptions([])
    }
  }, [open])

  return (
    <Autocomplete
      className={classes.root}
      id={id}
      open={open}
      onOpen={() => {
        setOpen(true)
      }}
      onClose={() => {
        setOpen(false)
      }}
      getOptionSelected={(option, value) => option.name === value.name}
      getOptionLabel={option => `${option.name} (${option.location})`}
      options={options}
      loading={loading}
      onChange={(evt, value) => onSelect(value)}
      renderInput={params => (
        <TextField
          {...params}
          label={
            <div className={classes.placeholder}>
              <SpaceportIcon arrival={arrival} className={classes.icon} />
              <div className={classes.bound}>
                {arrival ? 'arrival' : 'departure'}
              </div>
            </div>
          }
          variant='outlined'
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <React.Fragment>
                {loading ? (
                  <CircularProgress color='inherit' size={20} />
                ) : null}
                {params.InputProps.endAdornment}
              </React.Fragment>
            )
          }}
        />
      )}
    />
  )
}

function SpaceportIcon ({ arrival, className }) {
  if (arrival !== true) {
    return <FlightTakeoffIcon className={className} />
  }
  return <FlightLandIcon className={className} />
}

export default SpacePortAutocomplete
