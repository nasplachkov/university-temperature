university-temperature
======================

The application provides REST based interface for getting the parsed temperature data of certain places.

## IMPORTANT ##
Make sure that you make [**twitter4j.properties**](http://twitter4j.org/en/configuration.html) file and also it must be in your classpath.
The file must include **consumerKey** and **consumerSecret**! Nothing else!

### REST APIs###
- /rest/get/
- /rest/get/name?q=NAME
        NAME - the name of the location to search for
- /rest/get/type?q=TYPE
        TYPE - can be either "temperature" or "humidity"
- /rest/get/location?lat=LATITUDE&long=LONGITUDE&rad=RADIUS&type=TYPE
        LATITUDE - GPS coords
        LONGITUDE - GPS coords
        RADIUS - filter locations by a radius (km) **NOT REQUIRED**
        TYPE - the same as in the previous API

### Applications ###
- /twitter
        This application asks the user to login in his Twitter profile and posts there the parsed data from the netlab locations regularly in a specified amount of time by the user.
- /maps
        Opens a Google Maps and puts markers on it with the locations that are providing data.
- /logs
        Uses Google Charts API to log the specified type of data and draw a chart, which contains the log results. Also shows minimum, maximum and average values depending on the data type.