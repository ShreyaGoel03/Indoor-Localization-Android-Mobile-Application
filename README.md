# Indoor-Localization-Android-Mobile-Application

### Application Activity
<p align = "justify">
An indoor localization app that will provide room level accuracy. <br/>
They are: <br/>
1) Get the WiFi scan results to know the list of access points nearby and their RSSI values <br/>
2) Showcase the list of APs that you can hear with their RSSI values to show this. <br/>
3) Wardive inside your home and get RSSI measurements of the APs from different rooms of your home using WiFi scan results. Store this information. Design this wardriving using appropriate functionality in the UI. For example, you have one UI control like
button that says now start your wardriving. <br/>
4) Given a test RSSI measurements of these APs, return your location. Think of a nice UI
to develop this. For example, again having one UI control says a button that lets you
test. During testing it will get RSSI of the APs nearby by getting the WiFi scan results.
Then, it will match it to stored information with a single point that is most similar to the
test data. <br/>
5) Also implementing the matching with KNN. <br/> 
</p>

### ScreenShots

#### First Page:

#### Permission and Wifi Scan to know Access Points and its strength.
<img src="https://user-images.githubusercontent.com/43794593/154324572-7ee2ea30-c720-48c8-9464-a897640e6d72.jpg" width=30% height=30%>    <img src="https://user-images.githubusercontent.com/43794593/154324581-24ba04eb-2713-4f00-a672-29accdcc1cb6.jpg" width=30% height=30%>       <img src="https://user-images.githubusercontent.com/43794593/154324587-52d54edc-029d-4163-9924-c8b6c45aae9d.jpg" width=30% height=30%>
<br/>
</br>
#### Collecting Training Data for Learn (On Clicking Learn Button) 
##### Save using Save Train button
<img src="https://user-images.githubusercontent.com/43794593/154324936-8f6156b2-c22e-480b-bd3e-e2017755ca4a.jpg" width=30% height=30%>    <img src="https://user-images.githubusercontent.com/43794593/154324943-6a1f4bb3-5cfb-4f90-8d4e-0092814cf36f.jpg" width=30% height=30%>     <img src="https://user-images.githubusercontent.com/43794593/154324965-cbc932e7-3cb7-40fc-950e-97fff173fb94.jpg" width=30% height=30%>
<br/>
<br/>
#### Testing
<img src="https://user-images.githubusercontent.com/43794593/154325239-6d106592-52cd-497a-a1f7-7928b53eccfc.jpg" width=30% height=30%>
<br/>

##### Locate Simply
<img src="https://user-images.githubusercontent.com/43794593/154325260-66030b5f-bc77-4fef-a5e8-152bdf409857.jpg" width=30% height=30%>
<br/>

##### Locate via KNN
<img src="https://user-images.githubusercontent.com/43794593/154325276-509dcb0c-1f5a-41c6-ad34-6ad6c9e978b8.jpg" width=30% height=30%>     <img src="https://user-images.githubusercontent.com/43794593/154325281-31a2dba1-259d-4ec4-ae39-80344a6aad7c.jpg" width=30% height=30%>
<br/>
<br/>
#### Database
<img src="https://user-images.githubusercontent.com/43794593/154325294-4084d05b-d393-457e-bffd-4969952fb737.png" width=50% height=50%>
<img src="https://user-images.githubusercontent.com/43794593/154325327-2bbf84e1-9ed1-47e1-942d-b07a205ac922.png" width=50% height=50%>
<img src="https://user-images.githubusercontent.com/43794593/154325326-7b96fd12-0cf0-47dc-addb-14baffb0f375.png" width=50% height=50%>



