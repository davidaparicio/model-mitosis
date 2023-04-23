import React, { useState } from 'react'
import { MuiPickersUtilsProvider } from '@material-ui/pickers'
import MomentUtils from '@date-io/moment'
import Card from '@material-ui/core/Card'
import CardActions from '@material-ui/core/CardActions'
import CardContent from '@material-ui/core/CardContent'
import Grid from '@material-ui/core/Grid'
import moment from 'moment'
import Button from '@material-ui/core/Button'
import { makeStyles } from '@material-ui/core/styles'
import { DateTimePicker } from '@material-ui/pickers'
import SpacePortAutocomplete from './SpacePortAutocomplete'
import fetch from 'cross-fetch'
import { withRouter } from 'react-router-dom'
import { Typography, Box } from '@material-ui/core'
import SearchTypeChooser from './SearchTypeChooser'
import { Alert } from '@material-ui/lab'

function SearchForm ({ history }) {
  const [outboundDate, setOutboundDate] = useState(moment().add(1, 'd'))
  const [inboundDate, setInboundDate] = useState(moment().add(7, 'd'))

  const useStyles = makeStyles(theme => ({
    grid: {
      height: '50vh',
      flexGrow: 1
    },
    card: {
      marginTop: theme.spacing(30)
    },
    title: {
      marginBottom: theme.spacing(1)
    },
    dateTimePicker: {}
  }))
  const classes = useStyles()

  const [departure, setDeparture] = useState()
  const [arrival, setArrival] = useState()
  const [error, setError] = useState()

  const [searchType, setSearchType] = useState('oneway')

  function performANewSearch () {
    const outbound = {
      departureSpacePortId: departure._links.self.href,
      departureSchedule: moment(outboundDate).format('YYYY-MM-DDTHH:mm'),
      arrivalSpacePortId: arrival._links.self.href
    }

    const inbound =
      searchType === 'roundtrip'
        ? {
            departureSpacePortId: arrival._links.self.href,
            departureSchedule: moment(inboundDate).format('YYYY-MM-DDTHH:mm'),
            arrivalSpacePortId: departure._links.self.href
          }
        : undefined

    const journeys = [outbound]
    if (inbound !== undefined) {
      journeys.push(inbound)
    }
    const request = { journeys }
    ;(async () => {
      const response = await fetch('/searches', {
        method: 'POST',
        body: JSON.stringify(request),
        headers: {
          'Content-Type': 'application/json'
        }
      })
      const search = await response.json()
      if (response.status === 201) {
        history.push(
          `/searches/${btoa(search._links.self.href)}/bound/outbound`
        )
      } else {
        setError(search.message)
      }
    })()
  }

  return (
    <MuiPickersUtilsProvider utils={MomentUtils}>
      <Grid
        container
        justify='center'
        alignContent='center'
        className={classes.grid}
        spacing={2}
      >
        <Grid item xs={4}>
          <Card className={classes.card}>
            <CardContent>
              <Typography variant='h6' className={classes.title}>
                Where do you want to go?
              </Typography>
              <SearchTypeChooser
                searchType={searchType}
                setSearchType={setSearchType}
              />
              <SpacePortAutocomplete
                id='outbound-departure'
                onSelect={setDeparture}
              />
              <SpacePortAutocomplete
                id='outbound-arrival'
                arrival
                onSelect={setArrival}
              />
              {error && <Alert severity='error'>{error}</Alert>}

              <Box display='flex' justifyContent='space-between' mt={2}>
                <DateTimePicker
                  key={'outbound-date'}
                  className={classes.dateTimePicker}
                  variant='inline'
                  label='Outbound Departure'
                  value={outboundDate}
                  onChange={setOutboundDate}
                />
                {searchType === 'roundtrip' && (
                  <DateTimePicker
                    key={'inbound-date'}
                    className={classes.dateTimePicker}
                    variant='inline'
                    label='Inbound Departure'
                    value={inboundDate}
                    onChange={setInboundDate}
                  />
                )}
              </Box>
            </CardContent>
            <CardActions>
              <Button
                fullWidth
                disabled={arrival === undefined || departure === undefined}
                size='medium'
                color='primary'
                variant='contained'
                onClick={performANewSearch}
              >
                Search
              </Button>
            </CardActions>
          </Card>
        </Grid>
      </Grid>
    </MuiPickersUtilsProvider>
  )
}

export default withRouter(SearchForm)
