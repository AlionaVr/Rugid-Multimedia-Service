package com.rugid.multimediaservice.adapter.in.grpc;
/*
import com.rugid.account.ImageDeleteRequest;
import com.rugid.account.ImageDeleteResponse;
import com.rugid.account.MultimediaServiceGrpc;
import com.rugid.multimediaservice.domain.port.in.DeleteFileUseCase;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class ImageServiceGrpc extends MultimediaServiceGrpc.MultimediaServiceImplBase {

    private final DeleteFileUseCase deleteFileUseCase;

    public ImageServiceGrpc(DeleteFileUseCase deleteFileUseCase) {
        this.deleteFileUseCase = deleteFileUseCase;
    }

    @Override
    public void delete(ImageDeleteRequest request, StreamObserver<ImageDeleteResponse> responseObserver) {
        String imageId = request.getImageId();
        DeleteFileUseCase.DeleteFileCommand deleteFileCommand = createDeleteFileCommand(imageId);

        deleteFileUseCase.delete(deleteFileCommand);

        ImageDeleteResponse response = ImageDeleteResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private DeleteFileUseCase.DeleteFileCommand createDeleteFileCommand(String imageId) {
        return new DeleteFileUseCase.DeleteFileCommand(imageId);
    }
}
*/