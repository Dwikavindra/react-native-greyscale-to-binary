import {View, Text, Image} from 'react-native';
import React, {useEffect, useState} from 'react';
import PixelsImage from '../../PixelsImage';
import ImagePicker from 'react-native-image-crop-picker';

const ImagePixels = () => {
  const [image, setImage] = useState(null);
  const [images, setImages] = useState(null);

  useEffect(() => {
    
  }, [])

  const pickSingleWithCamera = (cropping, mediaType = 'photo') => {
    ImagePicker.openCamera({
      cropping: cropping,
      width: 500,
      height: 500,
      includeExif: true,
      mediaType,
    })
      .then(image => {
        console.log('received image', image.path);
        setImage({
          uri: image.path,
          width: image.width,
          height: image.height,
          mime: image.mime,
          mime: image.data,
        });
        setImages(null);
      })
      .catch(e => alert(e));
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
        console.log('received base64 image', image);
        setImage({
          uri: `data:${image.mime};base64,` + image.data,
          width: image.width,
          height: image.height,
        });
        setImages(null);
      })
      .catch(e => alert(e));
  };

  const renderAsset = image => {
    return renderImage(image);
  };

  
  function testFunction() {
    PixelsImage.createBinaryPixels('testName', 'testLocation');
    console.log("goooooooooodddddddd");
  }

  return (
    <View style={{
      alignSelf: 'center',
      justifyContent: 'center',
      flexDirection: 'column',
      flex: 1,

    }}>
      <View style={{
        marginBottom: 20
      }}>
        <Image 
        source={image}
        style={{
          width: 150,
          height: 150,
          borderRadius: 20,
        }}
        />
      </View>
      
      <View style={{
        marginBottom: 10
      }}>
        <Text style={{
          fontSize: 20,
          backgroundColor: 'tomato',
          padding: 10,
          borderRadius: 20,
          textAlign: 'center'
        }} onPress={() => pickSingleWithCamera()}>clique</Text>
      </View>
      <View>
        <Text style={{
          fontSize: 20,
          backgroundColor: 'tomato',
          padding: 10,
          borderRadius: 20,
          textAlign: 'center'
        }} onPress={() => pickSingleBase64()}>clique</Text>
      </View>
    </View>
  );
};

export default ImagePixels;
