/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable prettier/prettier */
/* eslint-disable no-undef */
/* eslint-disable no-dupe-keys */
import {View, Text, Image, FlatList, StyleSheet, Dimensions, TouchableOpacity, LogBox} from 'react-native';
import React, {useEffect, useState} from 'react';
import PixelsImage from '../../PixelsImage';
import ImagePicker from 'react-native-image-crop-picker';
import RNFS from 'react-native-fs';

const start_width = Dimensions.get('screen').width;
const start_height = Dimensions.get('screen').height;

const ImagePixels = () => {
  const [image, setImage] = useState(null);
  const [images, setImages] = useState(null);
  const [newpath, setNewPath] = useState('');
  const [name, setName] = useState('');
  const [ext, setExt] = useState('');

  //create new folder for grayScale images
  const newFolderPath = RNFS.DownloadDirectoryPath + '/Assets/BinaryImage';
  const makeDirectory = async (newFolderPath) => {
    await RNFS.mkdir(newFolderPath); //create a new folder on folderPath
  };

  useEffect(() => {
    makeDirectory(newFolderPath); //execute this function on first mount
    getFileContent(RNFS.DownloadDirectoryPath); //this function was defined in the previous example
    LogBox.ignoreAllLogs(['Error: Attempt to get length of null array', 'WARN  Debugger and device times have drifted by more than 60s. Please correct this by running adb shell " `date +%m%d%H%M%Y.%S`']);
  }, [name]);

  //Getting file paths
  const [downloadsFolder, setDownloadsFolder] = useState('');
  const [documentsFolder, setDocumentsFolder] = useState('');
  const [externalDirectory, setExternalDirectory] = useState('');

  //get user's file paths from react-native-fs
  useEffect(() => {
    setDownloadsFolder(RNFS.DownloadDirectoryPath);
    setDocumentsFolder(RNFS.DocumentDirectoryPath); //alternative to MainBundleDirectory.
    setExternalDirectory(RNFS.ExternalStorageDirectoryPath);
  }, []);

  /* useEffect(() => {
    getFileContent(RNFS.DownloadDirectoryPath + '/assets'); //run the function on the first render.
  }, []);*/

  useEffect(() => {
    //let pathDir = RNFS.ExternalStorageDirectoryPath + '/Android/data/com.nativemodule/files/Pictures/';
    let pathDir = RNFS.ExternalStorageDirectoryPath
    ;
    getFileContent(pathDir); //run the function on the first render.
  }, [ext]);
  useEffect(() => {
    PixelsImage.createBinaryPixels(newpath, name, ext);
  }, [newpath]);

  //this component will render our list item to the UI
  const Item = ({ name, isFile }) => {
    return (
      <View>
        <Text style={styles.name}>Name: {name}</Text>
        <Text> {isFile ? 'It is a file' : "It's a folder"}</Text>
      </View>
    );
  };

  const renderItem = ({ item, index }) => {
    return (
      <View style={{
        padding: 10,
      }}>
        {
          files.length == null ?
          <>
          <Text>
            file not found
          </Text>
          </>
          :
          <>
          <Text style={styles.title}>{index}</Text><Item name={item.name} isFile={item.isFile()} />
          </>


        }

      </View>
    );
  };

  //Reading directories
  const [files, setFiles] = useState([]);
  const getFileContent = async (path) => {
    const reader = await RNFS.readDir(path);
    setFiles(reader);
  };

  const pickSingleBase64 = cropit => {
    ImagePicker.openPicker({
      width: 300,
      height: 300,
      cropping: cropit,
      includeBase64: true,
      includeExif: true,
    })
      .then(image => {
        setImage({
          uri: `data:${image.mime};base64,` + image.data,
        });
        console.log('image selected-----fff-', image.path);
        if (image.path.length != null){
           newImg = image.path;
          let uriArray = newImg.split('/0').pop();
          let uriImgName = newImg.split('/');
          let nameToChange = uriImgName[uriImgName.length - 1];
          setName(nameToChange.split('.')[0]);
          setExt(nameToChange.split('.')[1]);
          setNewPath(uriArray);
          console.log('relative path: ' + nameToChange.split('.')[0]);
          console.log('split application: ' + uriArray);
          console.log('split ext: ' + nameToChange.split('.')[1]);
        }
        setImages(null);
      })
      .catch(e => alert(e));
  };

  const renderAsset = image => {
    return renderImage(image);
  };

  function testFunction() {
    PixelsImage.createBinaryPixels(image);
    console.log('goooooooooodddddddd');
  }

  return (
    <View style={[styles.container, {
      flexDirection: 'column',
    }]}>
      <View style={{ flex: 2, backgroundColor: '#F2F3F4' }}>
      <View style={{ flexDirection: 'row', alignSelf: 'center', margin: 8, justifyContent: 'center' }}>
      <Image
        source={image}
        style={{
          width: 130,
          height: 130,
          borderRadius: 20,
        }}
        />
      </View>
      </View>
      <View style={{ flex: 2, backgroundColor: '#D7DBDD'}}>
        <View style={{ justifyContent: 'space-evenly', width: '50%', height: 40, alignSelf: 'center', flex: 1 }}>
        <View>
        <TouchableOpacity onPress={() => pickSingleBase64()} style={{backgroundColor: 'tomato', padding: 10, borderRadius: 12}}>
        <Text style={{
            fontSize: 12,
            textTransform: 'uppercase',
            textAlign: 'center',
          }}>
            choose a picture
          </Text>
        </TouchableOpacity>
        </View>
        <View>
          <Text style={{
            fontSize: 13,
            fontStyle: 'normal',
            fontWeight: '500',
            textTransform: 'capitalize',
            //textAlign: 'justify',
            marginBottom: 3,
          }}>
          after the image is displayed you can find it in the current directory of your device: binary image path <Text style={{fontSize: 12, fontStyle: 'italic', fontWeight: 'bold', color: 'red'}}>"Download/assets/grayscalemages"</Text>
          </Text>
          <Text style={{
            fontSize: 13,
            fontStyle: 'normal',
            fontWeight: '500',
            textTransform: 'capitalize',
            //textAlign: 'justify',
          }}>
          binary file path <Text style={{fontSize: 12, fontStyle: 'italic', fontWeight: 'bold', color: 'red'}}>"download/assets/binaryImage"</Text>
          </Text>
        </View>
        </View>
      </View>
      <View style={{ flex: 3, backgroundColor: '#D0D3D4' }}>
      <Text style={{
          fontSize: 18,
          fontStyle: 'normal',
          fontWeight: 'bold',
          textTransform: 'uppercase',
          justifyContent: 'center',
          textAlign: 'center',
        }}>
          list of files
        </Text>
        <FlatList
          data={files}
          renderItem={renderItem}
          keyExtractor={(item) => item.name}
        />
      </View>
    </View>
  );
};

export default ImagePixels;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: start_width,
    height: start_height,
  },
});
