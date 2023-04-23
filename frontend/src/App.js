import React from 'react'
import { makeStyles } from '@material-ui/core/styles'
import AppBar from '@material-ui/core/AppBar'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import logo from './columbiadexpress.png'
import SearchForm from './SearchForm/SearchForm'
import Search from './Search/Search'
import { createMuiTheme } from '@material-ui/core/styles'
import { ThemeProvider } from '@material-ui/styles'
import Link from '@material-ui/core/Link'

import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import Booking from './Booking/Booking'

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1
  },
  menuButton: {
    marginRight: theme.spacing(2)
  },
  title: {
    flexGrow: 1
  },
  logo: {
    width: '32px',
    marginRight: theme.spacing(1)
  }
}))

const theme = createMuiTheme({
  palette: {
    primary: {
      main: '#0B3D91'
    },
    secondary: {
      main: '#FC3D21'
    }
  }
})

function App () {
  const classes = useStyles()
  return (
    <ThemeProvider theme={theme}>
      <div className={classes.root}>
        <AppBar position='static'>
          <Toolbar>
            <img src={logo} className={classes.logo} alt='logo' />
            <Typography variant='h6' className={classes.title}>
              <Link href='/' color='inherit' underline='none'>
                Columbiad Express
              </Link>
            </Typography>
          </Toolbar>
        </AppBar>
        <Router>
          <Switch>
            <Route path='/searches/:searchId/bound/:bound'>
              <Search />
            </Route>
            <Route path='/bookings/:bookingId'>
              <Booking />
            </Route>
            <Route path='/'>
              <SearchForm />
            </Route>
          </Switch>
        </Router>
      </div>
    </ThemeProvider>
  )
}

export default App
