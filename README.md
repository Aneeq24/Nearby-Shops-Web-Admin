Nearby Shops 
[![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=Nearby%20Shops%20Open%20Source%20Food%20Delivery%20and%20Hyperlocal%20app&url=https://github.com/NearbyShops/Nearby-Shops-End-User-Android-app&via=moraysumeet&hashtags=opensource,androiddev,fooddelivery,android,ecommerce)
=============

<img src="https://github.com/SumeetMoray/Nearby-Shops-End-User-Android-app/blob/master/media/nearby-shops-logo-small.png" width="80">    <a href="https://play.google.com/store/apps/details?id=org.nearbyshops.enduserappnew&hl=en"><img class="alignnone" src="https://goldtonemusicgroup.com/img/goldtone/main-page/news/playstore-badge.png" alt="Get it on Google Play" width="130" height="40" /></a> <a href="https://twitter.com/nearbyshopsapp?ref_src=twsrc%5Etfw" class="twitter-follow-button" data-show-count="false">
<img src="https://www.mathlearningcenter.org/sites/default/files/images/Follow%20on%20Twitter.png"
width="120">
</a>


### Install with Docker 


    cd ~

    wget https://raw.githubusercontent.com/NearbyShops/Nearby-Shops-API/master/docker-compose.yml

    docker-compose up


Full installation guide available at https://developer.nearbyshops.org/installation/installation-guide-quick-docker.html



About Nearby Shops
--------------------

Nearby Shops is a Free Open-Source food delivery, grocery and hyperlocal app platform. You can install your self-hosted instance on Digital Ocean or AWS and get your grocery or food delivery market up and running in just 5 minutes.


Nearby Shops implements Alibaba's Grocery Online-to-Offline Concept. Customers can place and order from the app and pick it up in the store. Home Delivery is also available !

The installation guide and app customization guide is provided at http://developer.nearbyshops.org


Website :  https://nearbyshops.org | Developers Guide: http://developer.nearbyshops.org


<img src="https://nearbyshops.org/images/screenshots_usa/shops_list_san_francisco.png" width="208">   <img src="https://nearbyshops.org/images/screenshots_usa/order_detail_john_doe.png" width="208">   <img src="https://nearbyshops.org/images/items-in-shop-new.png" width="208"> 



## 🚩 Table of Contents
- [Third Party Integrations](#third-party-integrations)
- [Connect with Us](#community---need-help-)
- [Contributions Required](#contributions-welcome)
- [Development Instructions](#development-instructions)
- [License](#license)





Third Party Integrations
-------------------------
Please see our Integrations guide given in the Developer Section. 
https://developer.nearbyshops.org/integrations/e-mail-integration.html



Connect With us - Need Help ? 
------------------------

If you want any help regarding anything. Feel free to contact us -- send a message through our forum or you can simply post an issue. Your issues will not be ignored and you will surely receive help. 

Discourse Forum - https://forum.nearbyshops.org

Follow us Twitter - https://twitter.com/nearbyshopsapp

Facebook - https://www.facebook.com/nearbyshops

Facebook Group - https://www.facebook.com/groups/1144257952430940/



Development Instructions
-------------------------

This is a spring boot project developed in Intellij Idea Community Version.

Clone this repository and open the source code in the Latest Stable version of Intellij Idea Community Edition. 
Then Rename the api_config_example.properties to api_config.properties in the data folder and set the custom values. 

You need to create a Database for PostgreSQL and put your database name and database username and password in the config
file. 


Create Docker Image
--------------------

You can create a docker image with the instructions given in the docker_image_creation.odt file. 



License
=======

Copyright (c) 2020 Nearby Shops

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

